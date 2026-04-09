package com.interviewai.aigateway.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.common.BusinessException;
import com.interviewai.aigateway.config.AiGatewayConfig;
import com.interviewai.aigateway.dto.ChatRequest;
import com.interviewai.aigateway.dto.ChatResponse;
import com.interviewai.aigateway.dto.EmbeddingRequest;
import com.interviewai.aigateway.dto.EmbeddingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class AiGatewayService {

    @Autowired
    private AiGatewayConfig gatewayConfig;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USAGE_KEY_PREFIX = "ai:usage:";
    private static final String RATE_LIMIT_KEY_PREFIX = "ai:ratelimit:";

    public ChatResponse chat(ChatRequest request, Long userId) {
        // Validate content
        if (gatewayConfig.getContentFilter().isEnabled()) {
            validateContent(request.getMessage());
        }

        // Get provider config
        AiGatewayConfig.ProviderConfig providerConfig = getProviderConfig(request.getProvider(), request.getModel());

        // Build messages
        List<Map<String, String>> messages = buildMessages(request);

        // Call AI provider
        Map<String, Object> response = callAiProvider(providerConfig, request.getModel(), messages, request);

        // Parse response
        return parseChatResponse(response, request.getConversationId(), providerConfig.getName());
    }

    public EmbeddingResponse embedding(EmbeddingRequest request) {
        AiGatewayConfig.ProviderConfig providerConfig = getProviderConfig(request.getProvider(), request.getModel());

        Map<String, Object> response = callEmbeddingProvider(providerConfig, request.getModel(), request.getText());

        return parseEmbeddingResponse(response, providerConfig.getName());
    }

    public Map<String, Object> getUsageStats(Long userId) {
        String key = USAGE_KEY_PREFIX + userId;
        String statsJson = redisTemplate.opsForValue().get(key);

        Map<String, Object> stats = new HashMap<>();
        if (statsJson != null) {
            try {
                stats = objectMapper.readValue(statsJson, Map.class);
            } catch (Exception e) {
                log.error("Failed to parse usage stats", e);
            }
        }

        return stats;
    }

    private AiGatewayConfig.ProviderConfig getProviderConfig(String providerName, String model) {
        String targetProvider = providerName != null ? providerName : gatewayConfig.getRouter().getDefaultProvider();

        if (gatewayConfig.getRouter().getProviders() == null) {
            throw new BusinessException(3001, "AI provider not configured");
        }

        for (AiGatewayConfig.ProviderConfig provider : gatewayConfig.getRouter().getProviders()) {
            if (provider.getName().equals(targetProvider)) {
                if (model != null && provider.getModels() != null) {
                    for (AiGatewayConfig.ModelConfig modelConfig : provider.getModels()) {
                        if (modelConfig.getName().equals(model) && modelConfig.isEnabled()) {
                            return provider;
                        }
                    }
                }
                // Return provider if model not specified or found
                return provider;
            }
        }

        throw new BusinessException(3002, "AI provider not found: " + targetProvider);
    }

    private List<Map<String, String>> buildMessages(ChatRequest request) {
        List<Map<String, String>> messages = new ArrayList<>();

        // Support OpenAI-style messages array
        if (request.getMessages() != null && !request.getMessages().isEmpty()) {
            for (ChatRequest.Message msg : request.getMessages()) {
                if (msg.getContent() != null && !msg.getContent().isEmpty()) {
                    messages.add(Map.of("role", msg.getRole() != null ? msg.getRole() : "user", "content", msg.getContent()));
                }
            }
            return messages;
        }

        // Add history if provided
        if (request.getHistory() != null) {
            for (ChatRequest.Message msg : request.getHistory()) {
                messages.add(Map.of("role", msg.getRole(), "content", msg.getContent()));
            }
        }

        // Add current message
        if (request.getMessage() != null && !request.getMessage().isEmpty()) {
            messages.add(Map.of("role", "user", "content", request.getMessage()));
        }

        return messages;
    }

    private Map<String, Object> callAiProvider(AiGatewayConfig.ProviderConfig provider,
                                                String model,
                                                List<Map<String, String>> messages,
                                                ChatRequest request) {
        WebClient webClient = webClientBuilder
                .baseUrl(provider.getEndpoint())
                .build();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("messages", messages);

        if (request.getTemperature() != null) {
            requestBody.put("temperature", request.getTemperature());
        } else {
            requestBody.put("temperature", 0.7);
        }

        if (request.getMaxTokens() != null) {
            requestBody.put("max_tokens", request.getMaxTokens());
        }

        try {
            String response = webClient.post()
                    .uri("/services/aigc/text-generation/generation")
                    .header("Authorization", "Bearer " + provider.getApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            return objectMapper.readValue(response, Map.class);
        } catch (Exception e) {
            log.error("Failed to call AI provider: {}", e.getMessage(), e);
            throw new BusinessException(3003, "AI provider call failed: " + e.getMessage());
        }
    }

    private Map<String, Object> callEmbeddingProvider(AiGatewayConfig.ProviderConfig provider,
                                                        String model,
                                                        String text) {
        WebClient webClient = webClientBuilder
                .baseUrl(provider.getEndpoint())
                .build();

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", text
        );

        try {
            String response = webClient.post()
                    .uri("/services/aigc/text-embedding/text-embedding")
                    .header("Authorization", "Bearer " + provider.getApiKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            return objectMapper.readValue(response, Map.class);
        } catch (Exception e) {
            log.error("Failed to call embedding provider: {}", e.getMessage(), e);
            throw new BusinessException(3004, "Embedding provider call failed: " + e.getMessage());
        }
    }

    private ChatResponse parseChatResponse(Map<String, Object> response, String conversationId, String providerName) {
        ChatResponse.ChatResponseBuilder builder = ChatResponse.builder()
                .conversationId(conversationId != null ? conversationId : UUID.randomUUID().toString())
                .provider(providerName);

        try {
            if (response.containsKey("output")) {
                Map<String, Object> output = (Map<String, Object>) response.get("output");
                builder.content((String) output.get("text"));
            }

            if (response.containsKey("usage")) {
                Map<String, Object> usage = (Map<String, Object>) response.get("usage");
                builder.promptTokens(((Number) usage.get("prompt_tokens")).longValue());
                builder.completionTokens(((Number) usage.get("completion_tokens")).longValue());
                builder.usageTokens(((Number) usage.get("total_tokens")).longValue());
            }

            if (response.containsKey("request_id")) {
                builder.messageId((String) response.get("request_id"));
            }

            builder.finishReason("stop");
        } catch (Exception e) {
            log.error("Failed to parse chat response", e);
            throw new BusinessException(3005, "Failed to parse AI response");
        }

        return builder.build();
    }

    private EmbeddingResponse parseEmbeddingResponse(Map<String, Object> response, String providerName) {
        EmbeddingResponse.EmbeddingResponseBuilder builder = EmbeddingResponse.builder()
                .provider(providerName);

        try {
            if (response.containsKey("data")) {
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
                if (!dataList.isEmpty()) {
                    Map<String, Object> data = dataList.get(0);
                    builder.embedding(parseEmbeddingList(data.get("embedding")));
                }
            }

            if (response.containsKey("model")) {
                builder.model((String) response.get("model"));
            }

            if (response.containsKey("usage")) {
                Map<String, Object> usage = (Map<String, Object>) response.get("usage");
                builder.usageTokens(((Number) usage.get("total_tokens")).longValue());
            }

            builder.object("embedding");
        } catch (Exception e) {
            log.error("Failed to parse embedding response", e);
            throw new BusinessException(3006, "Failed to parse embedding response");
        }

        return builder.build();
    }

    @SuppressWarnings("unchecked")
    private List<Float> parseEmbeddingList(Object embeddingObj) {
        if (embeddingObj instanceof List) {
            List<Number> numbers = (List<Number>) embeddingObj;
            List<Float> floats = new ArrayList<>();
            for (Number num : numbers) {
                floats.add(num.floatValue());
            }
            return floats;
        }
        return new ArrayList<>();
    }

    private void validateContent(String content) {
        if (content == null || content.isEmpty()) {
            return;
        }

        List<String> blockList = gatewayConfig.getContentFilter().getBlockList();
        if (blockList != null) {
            for (String blocked : blockList) {
                if (content.contains(blocked)) {
                    throw new BusinessException(3007, "Content contains sensitive words");
                }
            }
        }
    }

    public boolean checkRateLimit(Long userId, String endpoint) {
        if (!gatewayConfig.getRateLimit().isEnabled()) {
            return true;
        }

        String key = RATE_LIMIT_KEY_PREFIX + userId + ":" + endpoint;
        String countStr = redisTemplate.opsForValue().get(key);

        int currentCount = countStr != null ? Integer.parseInt(countStr) : 0;
        int limit = getRateLimitForEndpoint(endpoint);

        return currentCount < limit;
    }

    public void incrementRateLimit(Long userId, String endpoint) {
        String key = RATE_LIMIT_KEY_PREFIX + userId + ":" + endpoint;
        redisTemplate.opsForValue().increment(key);

        // Set expiry to end of day
        long secondsUntilMidnight = (86400 - (System.currentTimeMillis() / 1000) % 86400);
        redisTemplate.expire(key, java.time.Duration.ofSeconds(secondsUntilMidnight));
    }

    private int getRateLimitForEndpoint(String endpoint) {
        List<AiGatewayConfig.RateLimitRule> rules = gatewayConfig.getRateLimit().getRules();
        if (rules != null) {
            for (AiGatewayConfig.RateLimitRule rule : rules) {
                if (endpoint.matches(rule.getEndpoint().replace("*", ".*"))) {
                    return rule.getQps();
                }
            }
        }
        return gatewayConfig.getRateLimit().getDefaultQps();
    }
}
