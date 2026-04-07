package com.interviewai.interview.controller;

import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.interview.dto.*;
import com.interviewai.interview.service.InterviewService;
import com.interviewai.interview.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/interview")
public class InterviewController {

    @Autowired
    private InterviewService interviewService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public Result<InterviewResponse> createInterview(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateInterviewRequest request) {
        Long userId = getUserIdFromToken(authHeader);
        InterviewResponse response = interviewService.createInterview(userId, request);
        return Result.success(response);
    }

    @GetMapping("/{id}")
    public Result<InterviewResponse> getInterview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        getUserIdFromToken(authHeader);
        InterviewResponse response = interviewService.getInterviewById(id);
        return Result.success(response);
    }

    @GetMapping("/list")
    public Result<List<InterviewResponse>> getInterviewList(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        List<InterviewResponse> responses = interviewService.getInterviewsByUserId(userId);
        return Result.success(responses);
    }

    @PutMapping("/{id}")
    public Result<InterviewResponse> updateInterview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody UpdateInterviewRequest request) {
        getUserIdFromToken(authHeader);
        InterviewResponse response = interviewService.updateInterview(id, request);
        return Result.success(response);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteInterview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        getUserIdFromToken(authHeader);
        interviewService.deleteInterview(id);
        return Result.success();
    }

    @PostMapping("/{id}/start")
    public Result<SessionResponse> startInterview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        SessionResponse response = interviewService.startInterviewSession(id, userId);
        return Result.success(response);
    }

    @PostMapping("/answer")
    public Result<QuestionResponse> submitAnswer(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SubmitAnswerRequest request) {
        Long userId = getUserIdFromToken(authHeader);
        QuestionResponse response = interviewService.submitAnswer(userId, request);
        return Result.success(response);
    }

    @GetMapping("/session/{sessionId}/questions")
    public Result<List<QuestionResponse>> getSessionQuestions(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long sessionId) {
        getUserIdFromToken(authHeader);
        List<QuestionResponse> responses = interviewService.getQuestionsBySessionId(sessionId);
        return Result.success(responses);
    }

    @PostMapping("/{id}/finish")
    public Result<InterviewResponse> finishInterview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        InterviewResponse response = interviewService.finishInterview(id, userId);
        return Result.success(response);
    }

    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(2001, "Token无效");
        }
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }
}
