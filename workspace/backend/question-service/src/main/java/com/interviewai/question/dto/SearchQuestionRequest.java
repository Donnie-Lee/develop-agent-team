package com.interviewai.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuestionRequest {

    private String keyword;

    private String type;

    private String difficulty;

    private String category;

    private String tags;

    private Integer page = 1;

    private Integer pageSize = 20;
}