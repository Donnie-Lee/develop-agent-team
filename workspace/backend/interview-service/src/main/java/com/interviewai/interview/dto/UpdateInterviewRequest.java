package com.interviewai.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInterviewRequest {

    private String interviewType;
    private String position;
    private Integer duration;
    private String status;
    private String summary;
    private String feedback;
}
