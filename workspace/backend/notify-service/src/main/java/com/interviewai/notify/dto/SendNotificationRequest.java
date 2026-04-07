package com.interviewai.notify.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "通知类型不能为空")
    private String type;

    @NotBlank(message = "通知标题不能为空")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    private String content;

    @NotBlank(message = "通知渠道不能为空")
    private String channel;

    @NotBlank(message = "接收者不能为空")
    private String receiver;
}
