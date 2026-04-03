package com.interviewai.aigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageStatsResponse {

    private Long userId;
    private Long totalTokens;
    private Long promptTokens;
    private Long completionTokens;
    private Long todayTokens;
    private Long conversationCount;
    private Long dailyLimit;
    private Long remainingTokens;
}
