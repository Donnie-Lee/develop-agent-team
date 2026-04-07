package com.interviewai.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewResponse {

    private Long id;
    private Long userId;
    private String interviewType;
    private String position;
    private Integer duration;
    private String status;
    private Integer totalScore;
    private Integer questionCount;
    private Integer answeredCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String summary;
    private String feedback;
    private LocalDateTime createdAt;
}
