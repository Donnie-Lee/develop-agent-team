package com.interviewai.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSectionDTO {

    private String sectionType; // basic, education, experience, skill

    private Object content; // JSON content

    private Integer sortOrder;
}
