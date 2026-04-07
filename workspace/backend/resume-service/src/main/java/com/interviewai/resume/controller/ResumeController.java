package com.interviewai.resume.controller;

import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.resume.dto.*;
import com.interviewai.resume.service.FileUploadService;
import com.interviewai.resume.service.ResumeService;
import com.interviewai.resume.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public Result<List<ResumeListResponse>> getResumeList(@RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        List<ResumeListResponse> response = resumeService.getResumeList(userId);
        return Result.success(response);
    }

    @PostMapping
    public Result<ResumeDetailResponse> createResume(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateResumeRequest request) {
        Long userId = getUserIdFromToken(authHeader);
        ResumeDetailResponse response = resumeService.createResume(userId, request);
        return Result.success(response);
    }

    @GetMapping("/{id}")
    public Result<ResumeDetailResponse> getResumeDetail(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        ResumeDetailResponse response = resumeService.getResumeDetail(userId, id);
        return Result.success(response);
    }

    @PutMapping("/{id}")
    public Result<ResumeDetailResponse> updateResume(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody UpdateResumeRequest request) {
        Long userId = getUserIdFromToken(authHeader);
        ResumeDetailResponse response = resumeService.updateResume(userId, id, request);
        return Result.success(response);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteResume(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        resumeService.deleteResume(userId, id);
        return Result.success();
    }

    @PostMapping("/upload")
    public Result<UploadResponse> uploadFile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "resumeId", required = false) Long resumeId) {
        Long userId = getUserIdFromToken(authHeader);

        // 上传文件
        String fileUrl = fileUploadService.uploadFile(file);

        // 更新简历文件URL
        UploadResponse response = resumeService.uploadFile(userId, resumeId, fileUrl);
        return Result.success(response);
    }

    @PostMapping("/{id}/parse")
    public Result<ParseResponse> parseResume(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        ParseResponse response = resumeService.parseResume(userId, id);
        return Result.success(response);
    }

    @GetMapping("/{id}/analyze")
    public Result<AnalyzeResponse> analyzeResume(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        AnalyzeResponse response = resumeService.analyzeResume(userId, id);
        return Result.success(response);
    }

    @GetMapping("/{id}/suggestions")
    public Result<SuggestionsResponse> getSuggestions(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        Long userId = getUserIdFromToken(authHeader);
        SuggestionsResponse response = resumeService.getSuggestions(userId, id);
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
