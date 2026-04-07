package com.interviewai.aigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret = "your-256-bit-secret-key-here-change-in-production";
    private long expiration = 86400000; // 24 hours in milliseconds
    private String header = "Authorization";
    private String prefix = "Bearer ";
}
