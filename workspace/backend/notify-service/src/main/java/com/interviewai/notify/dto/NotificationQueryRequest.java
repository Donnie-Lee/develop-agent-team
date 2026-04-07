package com.interviewai.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueryRequest {

    private Long userId;
    private String type;
    private String channel;
    private String status;
    private Integer page = 1;
    private Integer pageSize = 10;
}
