package com.interviewai.user.dto;

import lombok.Data;

@Data
public class UpdateUserInfoRequest {

    private String nickname;

    private String avatarUrl;
}
