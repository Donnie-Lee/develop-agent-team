package com.interviewai.aigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "ai.gateway")
public class AiGatewayConfig {

    private RouterConfig router = new RouterConfig();
    private RateLimitConfig rateLimit = new RateLimitConfig();
    private CircuitBreakerConfig circuitBreaker = new CircuitBreakerConfig();
    private BillingConfig billing = new BillingConfig();
    private ContentFilterConfig contentFilter = new ContentFilterConfig();

    @Data
    public static class RouterConfig {
        private String defaultProvider = "aliyun";
        private List<ProviderConfig> providers;
    }

    @Data
    public static class ProviderConfig {
        private String name;
        private String type;
        private String endpoint;
        private String apiKey;
        private List<ModelConfig> models;
    }

    @Data
    public static class ModelConfig {
        private String name;
        private boolean enabled = true;
        private int maxTokens = 8000;
        private double temperature = 0.7;
    }

    @Data
    public static class RateLimitConfig {
        private boolean enabled = true;
        private int defaultQps = 10;
        private int defaultDailyLimit = 1000;
        private List<RateLimitRule> rules;
    }

    @Data
    public static class RateLimitRule {
        private String endpoint;
        private int qps;
        private int dailyLimit;
    }

    @Data
    public static class CircuitBreakerConfig {
        private boolean enabled = true;
        private int failureRateThreshold = 50;
        private long waitDurationInOpenState = 60000;
        private int slidingWindowSize = 10;
    }

    @Data
    public static class BillingConfig {
        private boolean enabled = true;
        private boolean trackPerUser = true;
    }

    @Data
    public static class ContentFilterConfig {
        private boolean enabled = true;
        private List<String> blockList;
        private String replaceChar = "*";
    }
}
