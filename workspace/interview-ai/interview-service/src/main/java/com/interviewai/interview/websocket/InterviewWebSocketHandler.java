package com.interviewai.interview.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.common.BusinessException;
import com.interviewai.interview.entity.InterviewQuestion;
import com.interviewai.interview.entity.InterviewSession;
import com.interviewai.interview.repository.InterviewQuestionRepository;
import com.interviewai.interview.repository.InterviewSessionRepository;
import com.interviewai.interview.service.InterviewService;
import com.interviewai.interview.util.JwtUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InterviewWebSocketHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private InterviewQuestionRepository questionRepository;

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 存储用户会话连接
    private final Map<Long, String> userSessions = new ConcurrentHashMap<>();

    @MessageMapping("/interview/{sessionId}/join")
    public void joinInterview(
            @DestinationVariable Long sessionId,
            @Payload Map<String, String> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        String token = payload.get("token");
        if (token == null) {
            throw new BusinessException(2001, "Token不能为空");
        }

        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new BusinessException(2001, "Token无效");
        }

        InterviewSession session = sessionRepository.selectById(sessionId);
        if (session == null) {
            throw new BusinessException(3001, "会话不存在");
        }

        // 记录用户会话
        userSessions.put(userId, sessionId.toString());

        // 发送加入成功消息
        Map<String, Object> response = new HashMap<>();
        response.put("type", "join_success");
        response.put("sessionId", sessionId);
        response.put("userId", userId);
        messagingTemplate.convertAndSend("/topic/interview/" + sessionId, response);

        log.info("用户 {} 加入面试会话 {}", userId, sessionId);
    }

    @MessageMapping("/interview/{sessionId}/question")
    public void handleQuestion(
            @DestinationVariable Long sessionId,
            @Payload Map<String, String> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        String token = payload.get("token");
        if (token == null) {
            throw new BusinessException(2001, "Token不能为空");
        }

        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new BusinessException(2001, "Token无效");
        }

        InterviewSession session = sessionRepository.selectById(sessionId);
        if (session == null) {
            throw new BusinessException(3001, "会话不存在");
        }

        // 获取下一个问题
        Map<String, Object> response = new HashMap<>();
        response.put("type", "question");
        response.put("sessionId", sessionId);
        response.put("currentIndex", session.getCurrentQuestionIndex());

        // 发送问题到用户
        messagingTemplate.convertAndSend("/topic/interview/" + sessionId + "/questions", response);

        log.info("向用户 {} 发送面试问题，会话 {}", userId, sessionId);
    }

    @MessageMapping("/interview/{sessionId}/answer")
    public void handleAnswer(
            @DestinationVariable Long sessionId,
            @Payload Map<String, String> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        String token = payload.get("token");
        String answer = payload.get("answer");
        Long questionId = Long.parseLong(payload.get("questionId"));

        if (token == null || answer == null) {
            throw new BusinessException(1001, "参数不完整");
        }

        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new BusinessException(2001, "Token无效");
        }

        // 更新答案
        InterviewQuestion question = questionRepository.selectById(questionId);
        if (question != null) {
            question.setUserAnswer(answer);
            question.setStatus("answered");
            questionRepository.updateById(question);
        }

        // 发送答案确认
        Map<String, Object> response = new HashMap<>();
        response.put("type", "answer_received");
        response.put("questionId", questionId);
        response.put("status", "success");
        messagingTemplate.convertAndSend("/topic/interview/" + sessionId + "/answers", response);

        log.info("用户 {} 提交答案，会话 {}, 问题 {}", userId, sessionId, questionId);
    }

    @MessageMapping("/interview/{sessionId}/finish")
    public void handleFinish(
            @DestinationVariable Long sessionId,
            @Payload Map<String, String> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        String token = payload.get("token");

        if (token == null) {
            throw new BusinessException(1001, "Token不能为空");
        }

        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new BusinessException(2001, "Token无效");
        }

        // 结束面试
        interviewService.finishInterview(sessionId, userId);

        // 发送结束消息
        Map<String, Object> response = new HashMap<>();
        response.put("type", "interview_finished");
        response.put("sessionId", sessionId);
        messagingTemplate.convertAndSend("/topic/interview/" + sessionId, response);

        // 移除用户会话记录
        userSessions.remove(userId);

        log.info("用户 {} 结束面试会话 {}", userId, sessionId);
    }

    @MessageMapping("/interview/{sessionId}/leave")
    public void handleLeave(
            @DestinationVariable Long sessionId,
            @Payload Map<String, String> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        String token = payload.get("token");

        if (token == null) {
            throw new BusinessException(1001, "Token不能为空");
        }

        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            throw new BusinessException(2001, "Token无效");
        }

        // 发送离开消息
        Map<String, Object> response = new HashMap<>();
        response.put("type", "user_left");
        response.put("sessionId", sessionId);
        response.put("userId", userId);
        messagingTemplate.convertAndSend("/topic/interview/" + sessionId, response);

        // 移除用户会话记录
        userSessions.remove(userId);

        log.info("用户 {} 离开面试会话 {}", userId, sessionId);
    }

    // 发送错误消息给用户
    public void sendError(Long userId, String errorMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("type", "error");
        response.put("message", errorMessage);
        messagingTemplate.convertAndSend("/queue/errors", response);
    }

    // 广播消息给所有会话参与者
    public void broadcastToSession(Long sessionId, Map<String, Object> message) {
        messagingTemplate.convertAndSend("/topic/interview/" + sessionId, message);
    }
}
