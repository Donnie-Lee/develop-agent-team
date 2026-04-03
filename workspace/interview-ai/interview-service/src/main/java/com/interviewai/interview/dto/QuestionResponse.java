package com.interviewai.interview.dto;

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
    private Long interviewId;
    private Long sessionId;
    private String questionType;
    private String questionText;
    private String userAnswer;
    private Integer score;
    private String feedback;
    private Integer orderIndex;
    private String status;
}
