package com.interviewai.aigateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    private String message;

    private String conversationId;

    @NotBlank(message = "模型名称不能为空")
    private String model;

    private String provider;

    private Double temperature;

    private Integer maxTokens;

    private Map<String, Object> extraParams;

    private List<Message> messages;

    private List<Message> history;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
