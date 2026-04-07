package com.interviewai.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String content;
    private String channel;
    private String status;
    private String receiver;
    private String createdAt;
}
