package com.interviewai.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeListResponse {

    private Long id;
    private String title;
    private Boolean isDefault;
    private String updatedAt;
}
