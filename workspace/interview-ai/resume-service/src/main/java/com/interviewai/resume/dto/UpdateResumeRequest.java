package com.interviewai.resume.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateResumeRequest {

    private String title;

    private List<ResumeSectionDTO> sections;
}
