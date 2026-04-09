package com.interviewai.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewReportResponse {

    private Long interviewId;
    private String position;
    private String interviewType;
    private String summary;
    private List<String> strengths;
    private List<String> weaknesses;
    private String recommendation;
    private Integer overallScore;
    private Integer questionCount;
    private Integer answeredCount;
    private String generatedAt;
}
