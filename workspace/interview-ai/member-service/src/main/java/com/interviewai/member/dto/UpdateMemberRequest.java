package com.interviewai.member.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateMemberRequest {

    @Positive(message = "会员等级必须为正数")
    private Integer level;

    private String description;

    private Integer status;

    private BigDecimal totalAmount;
}
