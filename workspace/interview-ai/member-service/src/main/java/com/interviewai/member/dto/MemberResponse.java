package com.interviewai.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private Long userId;
    private Integer level;
    private LocalDateTime memberSince;
    private LocalDateTime expireTime;
    private Integer status;
    private BigDecimal totalAmount;
    private String description;
    private String statusName;
}
