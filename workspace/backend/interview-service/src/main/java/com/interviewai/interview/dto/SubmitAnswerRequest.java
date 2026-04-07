package com.interviewai.interview.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerRequest {

    @NotBlank(message = "会话ID不能为空")
    private Long sessionId;

    @NotBlank(message = "问题ID不能为空")
    private Long questionId;

    @NotBlank(message = "回答不能为空")
    private String answer;
}
