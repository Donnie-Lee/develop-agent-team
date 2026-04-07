package com.interviewai.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {

    private Long id;
    private String title;
    private String content;
    private String type;
    private String difficulty;
    private String category;
    private String tags;
    private String answer;
    private String explanation;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private String createdAt;
    private String updatedAt;
}