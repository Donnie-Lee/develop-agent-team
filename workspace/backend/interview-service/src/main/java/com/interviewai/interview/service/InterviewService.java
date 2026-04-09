package com.interviewai.interview.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.interviewai.common.BusinessException;
import com.interviewai.interview.dto.*;
import com.interviewai.interview.entity.Interview;
import com.interviewai.interview.entity.InterviewQuestion;
import com.interviewai.interview.entity.InterviewSession;
import com.interviewai.interview.repository.InterviewQuestionRepository;
import com.interviewai.interview.repository.InterviewRepository;
import com.interviewai.interview.repository.InterviewSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InterviewService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private InterviewQuestionRepository questionRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AiQuestionService aiQuestionService;

    private static final String INTERVIEW_CACHE_PREFIX = "interview:";

    @Transactional
    public InterviewResponse createInterview(Long userId, CreateInterviewRequest request) {
        Interview interview = new Interview();
        interview.setUserId(userId);
        interview.setInterviewType(request.getInterviewType());
        interview.setPosition(request.getPosition());
        interview.setDuration(request.getDuration() != null ? request.getDuration() : 30);
        interview.setStatus("created");
        interview.setQuestionCount(0);
        interview.setAnsweredCount(0);

        interviewRepository.insert(interview);

        // 创建默认面试会话
        InterviewSession session = new InterviewSession();
        session.setInterviewId(interview.getId());
        session.setUserId(userId);
        session.setSessionType(request.getInterviewType());
        session.setStatus("active");
        session.setCurrentQuestionIndex(0);
        sessionRepository.insert(session);

        return toInterviewResponse(interview);
    }

    public InterviewResponse getInterviewById(Long interviewId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }
        return toInterviewResponse(interview);
    }

    public List<InterviewResponse> getInterviewsByUserId(Long userId) {
        LambdaQueryWrapper<Interview> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Interview::getUserId, userId);
        queryWrapper.orderByDesc(Interview::getCreatedAt);
        List<Interview> interviews = interviewRepository.selectList(queryWrapper);
        return interviews.stream().map(this::toInterviewResponse).collect(Collectors.toList());
    }

    @Transactional
    public InterviewResponse updateInterview(Long interviewId, UpdateInterviewRequest request) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }

        if (request.getInterviewType() != null) {
            interview.setInterviewType(request.getInterviewType());
        }
        if (request.getPosition() != null) {
            interview.setPosition(request.getPosition());
        }
        if (request.getDuration() != null) {
            interview.setDuration(request.getDuration());
        }
        if (request.getStatus() != null) {
            interview.setStatus(request.getStatus());
        }
        if (request.getSummary() != null) {
            interview.setSummary(request.getSummary());
        }
        if (request.getFeedback() != null) {
            interview.setFeedback(request.getFeedback());
        }

        interviewRepository.updateById(interview);

        // 清除缓存
        redisTemplate.delete(INTERVIEW_CACHE_PREFIX + interviewId);

        return toInterviewResponse(interview);
    }

    @Transactional
    public void deleteInterview(Long interviewId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }

        // 逻辑删除
        LambdaUpdateWrapper<Interview> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Interview::getId, interviewId)
                .set(Interview::getDeletedAt, LocalDateTime.now());
        interviewRepository.update(null, updateWrapper);

        // 清除缓存
        redisTemplate.delete(INTERVIEW_CACHE_PREFIX + interviewId);
    }

    @Transactional
    public SessionResponse startInterviewSession(Long interviewId, Long userId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }
        if (!interview.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该面试");
        }

        // 更新面试状态
        interview.setStatus("in_progress");
        interview.setStartTime(LocalDateTime.now());
        interviewRepository.updateById(interview);

        // 获取或创建活跃会话
        LambdaQueryWrapper<InterviewSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterviewSession::getInterviewId, interviewId)
                .eq(InterviewSession::getStatus, "active");
        InterviewSession session = sessionRepository.selectOne(queryWrapper);

        if (session == null) {
            session = new InterviewSession();
            session.setInterviewId(interviewId);
            session.setUserId(userId);
            session.setSessionType(interview.getInterviewType());
            session.setStatus("active");
            session.setCurrentQuestionIndex(0);
            session.setStartedAt(LocalDateTime.now());
            sessionRepository.insert(session);
        } else {
            session.setStartedAt(LocalDateTime.now());
            sessionRepository.updateById(session);
        }

        return toSessionResponse(session);
    }

    @Transactional
    public QuestionResponse submitAnswer(Long userId, SubmitAnswerRequest request) {
        InterviewQuestion question = questionRepository.selectById(request.getQuestionId());
        if (question == null) {
            throw new BusinessException(3001, "问题不存在");
        }

        InterviewSession session = sessionRepository.selectById(request.getSessionId());
        if (session == null) {
            throw new BusinessException(3001, "会话不存在");
        }
        if (!session.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该会话");
        }

        // 更新答案
        question.setUserAnswer(request.getAnswer());
        question.setStatus("answered");
        questionRepository.updateById(question);

        // 更新会话进度
        session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
        sessionRepository.updateById(session);

        // 更新面试回答数
        Interview interview = interviewRepository.selectById(session.getInterviewId());
        if (interview != null) {
            interview.setAnsweredCount(interview.getAnsweredCount() + 1);
            interviewRepository.updateById(interview);
        }

        return toQuestionResponse(question);
    }

    public List<QuestionResponse> getQuestionsBySessionId(Long sessionId) {
        LambdaQueryWrapper<InterviewQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterviewQuestion::getSessionId, sessionId)
                .orderByAsc(InterviewQuestion::getOrderIndex);
        List<InterviewQuestion> questions = questionRepository.selectList(queryWrapper);
        return questions.stream().map(this::toQuestionResponse).collect(Collectors.toList());
    }

    public List<QuestionResponse> getQuestionsByInterviewId(Long interviewId, Long userId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }
        if (!interview.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该面试");
        }

        // Find active session for this interview
        LambdaQueryWrapper<InterviewSession> sessionQueryWrapper = new LambdaQueryWrapper<>();
        sessionQueryWrapper.eq(InterviewSession::getInterviewId, interviewId)
                .eq(InterviewSession::getStatus, "active");
        InterviewSession session = sessionRepository.selectOne(sessionQueryWrapper);

        if (session == null) {
            // No active session, return empty list or throw exception
            return List.of();
        }

        return getQuestionsBySessionId(session.getId());
    }

    @Transactional
    public InterviewResponse finishInterview(Long interviewId, Long userId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }
        if (!interview.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该面试");
        }

        interview.setStatus("completed");
        interview.setEndTime(LocalDateTime.now());

        // 计算总分
        LambdaQueryWrapper<InterviewQuestion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterviewQuestion::getInterviewId, interviewId);
        List<InterviewQuestion> questions = questionRepository.selectList(queryWrapper);

        int totalScore = questions.stream()
                .filter(q -> q.getScore() != null)
                .mapToInt(InterviewQuestion::getScore)
                .sum();
        interview.setTotalScore(totalScore);

        interviewRepository.updateById(interview);

        // 结束所有活跃会话
        LambdaUpdateWrapper<InterviewSession> sessionUpdateWrapper = new LambdaUpdateWrapper<>();
        sessionUpdateWrapper.eq(InterviewSession::getInterviewId, interviewId)
                .eq(InterviewSession::getStatus, "active")
                .set(InterviewSession::getStatus, "completed")
                .set(InterviewSession::getEndedAt, LocalDateTime.now());
        sessionRepository.update(null, sessionUpdateWrapper);

        // 清除缓存
        redisTemplate.delete(INTERVIEW_CACHE_PREFIX + interviewId);

        return toInterviewResponse(interview);
    }

    @Transactional
    public QuestionResponse generateQuestions(Long interviewId, Long userId, String position, String interviewType) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }
        if (!interview.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该面试");
        }

        // Get or create active session
        InterviewSession session = getOrCreateSession(interviewId, userId, interviewType);

        // Generate questions using AI
        List<InterviewQuestion> questions = aiQuestionService.generateQuestions(interviewId, userId, position, interviewType);

        if (!questions.isEmpty()) {
            return toQuestionResponse(questions.get(0));
        }

        throw new BusinessException(3001, "问题生成失败");
    }

    @Transactional
    public QuestionResponse generateFollowUpQuestion(Long interviewId, Long sessionId, Long userId,
                                                    String previousQuestion, String previousAnswer) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }

        InterviewSession session = sessionRepository.selectById(sessionId);
        if (session == null) {
            throw new BusinessException(3001, "会话不存在");
        }
        if (!session.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该会话");
        }

        // Generate follow-up question
        InterviewQuestion followUp = aiQuestionService.generateFollowUpQuestion(
                interviewId, sessionId, userId, previousQuestion, previousAnswer);

        // Update session progress
        session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
        sessionRepository.updateById(session);

        // Save to conversation history
        aiQuestionService.saveConversationHistory(sessionId, "user", previousAnswer);
        aiQuestionService.saveConversationHistory(sessionId, "assistant", followUp.getQuestionText());

        return toQuestionResponse(followUp);
    }

    public InterviewReportResponse generateReport(Long interviewId, Long userId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }
        if (!interview.getUserId().equals(userId)) {
            throw new BusinessException(3002, "无权限访问该面试");
        }

        // Generate report using AI
        Map<String, Object> reportData = aiQuestionService.generateReport(interviewId);

        // Build response
        return InterviewReportResponse.builder()
                .interviewId(interviewId)
                .position(interview.getPosition())
                .interviewType(interview.getInterviewType())
                .summary((String) reportData.get("summary"))
                .strengths((List<String>) reportData.get("strengths"))
                .weaknesses((List<String>) reportData.get("weaknesses"))
                .recommendation((String) reportData.get("recommendation"))
                .overallScore((Integer) reportData.get("overallScore"))
                .questionCount((Integer) reportData.get("questionCount"))
                .answeredCount((Integer) reportData.get("answeredCount"))
                .generatedAt((String) reportData.get("generatedAt"))
                .build();
    }

    @Transactional
    public Map<String, Object> analyzeAnswer(Long interviewId, Long questionId, String answer) {
        InterviewQuestion question = questionRepository.selectById(questionId);
        if (question == null) {
            throw new BusinessException(3001, "问题不存在");
        }

        // Analyze answer using AI
        Map<String, Object> analysis = aiQuestionService.analyzeAnswer(question.getQuestionText(), answer);

        // Update question with answer and score
        question.setUserAnswer(answer);
        question.setStatus("answered");

        if (analysis.containsKey("score")) {
            question.setScore((Integer) analysis.get("score"));
        }
        if (analysis.containsKey("feedback")) {
            question.setFeedback((String) analysis.get("feedback"));
        }

        questionRepository.updateById(question);

        // Update interview answered count
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview != null) {
            interview.setAnsweredCount(interview.getAnsweredCount() + 1);
            interviewRepository.updateById(interview);
        }

        // Update session progress
        LambdaQueryWrapper<InterviewSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterviewSession::getInterviewId, interviewId)
                .eq(InterviewSession::getStatus, "active");
        InterviewSession session = sessionRepository.selectOne(queryWrapper);
        if (session != null) {
            session.setCurrentQuestionIndex(session.getCurrentQuestionIndex() + 1);
            sessionRepository.updateById(session);
        }

        return analysis;
    }

    private InterviewSession getOrCreateSession(Long interviewId, Long userId, String interviewType) {
        LambdaQueryWrapper<InterviewSession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterviewSession::getInterviewId, interviewId)
                .eq(InterviewSession::getStatus, "active");
        InterviewSession session = sessionRepository.selectOne(queryWrapper);

        if (session == null) {
            session = new InterviewSession();
            session.setInterviewId(interviewId);
            session.setUserId(userId);
            session.setSessionType(interviewType);
            session.setStatus("active");
            session.setCurrentQuestionIndex(0);
            session.setStartedAt(LocalDateTime.now());
            sessionRepository.insert(session);
        }

        return session;
    }

    private InterviewResponse toInterviewResponse(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .userId(interview.getUserId())
                .interviewType(interview.getInterviewType())
                .position(interview.getPosition())
                .duration(interview.getDuration())
                .status(interview.getStatus())
                .totalScore(interview.getTotalScore())
                .questionCount(interview.getQuestionCount())
                .answeredCount(interview.getAnsweredCount())
                .startTime(interview.getStartTime())
                .endTime(interview.getEndTime())
                .summary(interview.getSummary())
                .feedback(interview.getFeedback())
                .createdAt(interview.getCreatedAt())
                .build();
    }

    private SessionResponse toSessionResponse(InterviewSession session) {
        return SessionResponse.builder()
                .id(session.getId())
                .interviewId(session.getInterviewId())
                .userId(session.getUserId())
                .sessionType(session.getSessionType())
                .status(session.getStatus())
                .currentQuestionIndex(session.getCurrentQuestionIndex())
                .startedAt(session.getStartedAt())
                .endedAt(session.getEndedAt())
                .createdAt(session.getCreatedAt())
                .build();
    }

    private QuestionResponse toQuestionResponse(InterviewQuestion question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .interviewId(question.getInterviewId())
                .sessionId(question.getSessionId())
                .questionType(question.getQuestionType())
                .questionText(question.getQuestionText())
                .userAnswer(question.getUserAnswer())
                .score(question.getScore())
                .feedback(question.getFeedback())
                .orderIndex(question.getOrderIndex())
                .status(question.getStatus())
                .build();
    }
}
