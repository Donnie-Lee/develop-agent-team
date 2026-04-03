package com.interviewai.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDetailResponse {

    private Long id;
    private String title;
    private Boolean isDefault;
    private Integer parseStatus;
    private Object parseResult;
    private List<ResumeSectionDTO> sections;
    private String updatedAt;
}
