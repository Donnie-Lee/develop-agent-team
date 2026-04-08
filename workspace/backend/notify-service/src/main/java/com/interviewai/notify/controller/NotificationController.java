package com.interviewai.notify.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.notify.dto.NotificationQueryRequest;
import com.interviewai.notify.dto.NotificationResponse;
import com.interviewai.notify.dto.SendNotificationRequest;
import com.interviewai.notify.service.NotificationService;
import com.interviewai.notify.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send")
    public Result<NotificationResponse> send(@Valid @RequestBody SendNotificationRequest request) {
        NotificationResponse response = notificationService.send(request);
        return Result.success(response);
    }

    @GetMapping("/{id}")
    public Result<NotificationResponse> getById(@PathVariable("id") Long id) {
        NotificationResponse response = notificationService.getById(id);
        return Result.success(response);
    }

    @GetMapping("/list")
    public Result<IPage<NotificationResponse>> query(NotificationQueryRequest request) {
        IPage<NotificationResponse> result = notificationService.query(request);
        return Result.success(result);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        notificationService.delete(id);
        return Result.success();
    }
}
