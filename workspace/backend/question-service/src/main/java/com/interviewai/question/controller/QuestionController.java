package com.interviewai.question.controller;

import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.question.dto.CreateQuestionRequest;
import com.interviewai.question.dto.QuestionResponse;
import com.interviewai.question.dto.SearchQuestionRequest;
import com.interviewai.question.dto.UpdateQuestionRequest;
import com.interviewai.question.service.QuestionService;
import com.interviewai.question.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public Result<QuestionResponse> createQuestion(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CreateQuestionRequest request) {
        QuestionResponse response = questionService.createQuestion(request);
        return Result.success(response);
    }

    @PutMapping("/{id}")
    public Result<QuestionResponse> updateQuestion(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateQuestionRequest request) {
        QuestionResponse response = questionService.updateQuestion(id, request);
        return Result.success(response);
    }

    @GetMapping("/{id}")
    public Result<QuestionResponse> getQuestion(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") Long id) {
        QuestionResponse response = questionService.getQuestion(id);
        return Result.success(response);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteQuestion(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable("id") Long id) {
        questionService.deleteQuestion(id);
        return Result.success();
    }

    @GetMapping
    public Result<List<QuestionResponse>> listQuestions(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "1",name = "page") Integer page,
            @RequestParam(defaultValue = "20",name = "pageSize") Integer pageSize) {
        List<QuestionResponse> response = questionService.listQuestions(page, pageSize);
        return Result.success(response);
    }

    @GetMapping("/search")
    public Result<List<QuestionResponse>> searchQuestions(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @ModelAttribute SearchQuestionRequest request) {
        List<QuestionResponse> response = questionService.searchQuestions(request);
        return Result.success(response);
    }
}
