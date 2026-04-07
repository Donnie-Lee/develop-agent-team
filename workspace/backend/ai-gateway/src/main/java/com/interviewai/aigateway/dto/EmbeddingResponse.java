package com.interviewai.aigateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingResponse {

    private String object;
    private List<Float> embedding;
    private String model;
    private String provider;
    private Long usageTokens;
}
