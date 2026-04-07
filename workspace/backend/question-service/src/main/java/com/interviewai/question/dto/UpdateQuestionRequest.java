package com.interviewai.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateQuestionRequest {

    private String title;

    private String content;

    private String type;

    private String difficulty;

    private String category;

    private String tags;

    private String answer;

    private String explanation;
}