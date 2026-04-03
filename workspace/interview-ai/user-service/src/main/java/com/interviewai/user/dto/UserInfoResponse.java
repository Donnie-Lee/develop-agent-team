package com.interviewai.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String phone;
    private String nickname;
    private String avatarUrl;
    private Integer memberLevel;
    private String memberExpire;
}
