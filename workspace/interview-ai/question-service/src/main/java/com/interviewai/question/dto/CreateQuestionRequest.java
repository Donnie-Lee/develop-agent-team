package com.interviewai.question.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionRequest {

    @NotBlank(message = "题目标题不能为空")
    private String title;

    @NotBlank(message = "题目内容不能为空")
    private String content;

    @NotBlank(message = "题目类型不能为空")
    private String type;

    @NotBlank(message = "难度不能为空")
    private String difficulty;

    private String category;

    private String tags;

    private String answer;

    private String explanation;
}