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
public class CreateInterviewRequest {

    @NotBlank(message = "面试类型不能为空")
    private String interviewType;

    @NotBlank(message = "职位不能为空")
    private String position;

    private Integer duration;

    private String resumeText;
}
