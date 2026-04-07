package com.interviewai.aigateway.controller;

import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.aigateway.dto.ChatRequest;
import com.interviewai.aigateway.dto.ChatResponse;
import com.interviewai.aigateway.dto.EmbeddingRequest;
import com.interviewai.aigateway.dto.EmbeddingResponse;
import com.interviewai.aigateway.dto.UsageStatsResponse;
import com.interviewai.aigateway.service.AiGatewayService;
import com.interviewai.aigateway.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/ai")
public class AiGatewayController {

    @Autowired
    private AiGatewayService aiGatewayService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Chat completion endpoint
     */
    @PostMapping("/chat")
    public Result<ChatResponse> chat(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody ChatRequest request) {

        Long userId = extractUserId(authHeader);

        // Check rate limit
        if (!aiGatewayService.checkRateLimit(userId, "/api/v1/ai/chat")) {
            throw new BusinessException(3008, "Rate limit exceeded");
        }

        ChatResponse response = aiGatewayService.chat(request, userId);

        // Increment rate limit counter
        aiGatewayService.incrementRateLimit(userId, "/api/v1/ai/chat");

        return Result.success(response);
    }

    /**
     * Streaming chat completion endpoint
     */
    @PostMapping("/chat/stream")
    public Result<Map<String, String>> chatStream(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody ChatRequest request) {

        Long userId = extractUserId(authHeader);

        if (!aiGatewayService.checkRateLimit(userId, "/api/v1/ai/chat/stream")) {
            throw new BusinessException(3008, "Rate limit exceeded");
        }

        // For streaming, we return a placeholder - actual streaming implementation
        // would use Server-Sent Events (SSE) or WebSocket
        Map<String, String> result = Map.of(
                "status", "streaming_not_implemented",
                "message", "Please use /api/v1/ai/chat for non-streaming requests"
        );

        return Result.success(result);
    }

    /**
     * Embedding endpoint for vector search
     */
    @PostMapping("/embedding")
    public Result<EmbeddingResponse> embedding(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @Valid @RequestBody EmbeddingRequest request) {

        Long userId = extractUserId(authHeader);

        EmbeddingResponse response = aiGatewayService.embedding(request);

        return Result.success(response);
    }

    /**
     * Get usage statistics for current user
     */
    @GetMapping("/usage")
    public Result<UsageStatsResponse> getUsageStats(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        Long userId = extractUserId(authHeader);

        Map<String, Object> stats = aiGatewayService.getUsageStats(userId);

        UsageStatsResponse response = UsageStatsResponse.builder()
                .userId(userId)
                .totalTokens(stats.get("totalTokens") != null ? ((Number) stats.get("totalTokens")).longValue() : 0L)
                .promptTokens(stats.get("promptTokens") != null ? ((Number) stats.get("promptTokens")).longValue() : 0L)
                .completionTokens(stats.get("completionTokens") != null ? ((Number) stats.get("completionTokens")).longValue() : 0L)
                .todayTokens(stats.get("todayTokens") != null ? ((Number) stats.get("todayTokens")).longValue() : 0L)
                .conversationCount(stats.get("conversationCount") != null ? ((Number) stats.get("conversationCount")).longValue() : 0L)
                .dailyLimit(1000L)
                .remainingTokens(1000L - (stats.get("todayTokens") != null ? ((Number) stats.get("todayTokens")).longValue() : 0L))
                .build();

        return Result.success(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public Result<Map<String, String>> health() {
        Map<String, String> status = Map.of(
                "status", "healthy",
                "service", "ai-gateway",
                "version", "1.0.0"
        );
        return Result.success(status);
    }

    /**
     * List available models
     */
    @GetMapping("/models")
    public Result<Map<String, Object>> listModels() {
        // This would return available models from config
        Map<String, Object> models = Map.of(
                "providers", Map.of(
                        "aliyun", Map.of(
                                "type", "qwen",
                                "models", new String[]{"qwen-turbo", "qwen-plus"}
                        ),
                        "zhipu", Map.of(
                                "type", "glm",
                                "models", new String[]{"glm-4"}
                        )
                )
        );
        return Result.success(models);
    }

    private Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // For development/testing, return a default user ID
            // In production, this should throw an exception
            return 0L;
        }

        String token = authHeader.replace("Bearer ", "");
        try {
            return jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            log.warn("Failed to extract user ID from token", e);
            return 0L;
        }
    }
}
