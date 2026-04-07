package com.interviewai.notify.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.interviewai.common.BusinessException;
import com.interviewai.notify.dto.NotificationQueryRequest;
import com.interviewai.notify.dto.NotificationResponse;
import com.interviewai.notify.dto.SendNotificationRequest;
import com.interviewai.notify.entity.Notification;
import com.interviewai.notify.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public NotificationResponse send(SendNotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .channel(request.getChannel())
                .receiver(request.getReceiver())
                .status("PENDING")
                .build();

        notificationRepository.insert(notification);

        sendToRocketMQ(notification);

        return toResponse(notification);
    }

    private void sendToRocketMQ(Notification notification) {
        try {
            rocketMQTemplate.convertAndSend("interview-ai-notify", notification);
            notification.setStatus("SENT");
            notificationRepository.updateById(notification);
            log.info("Notification sent to RocketMQ: id={}", notification.getId());
        } catch (Exception e) {
            log.error("Failed to send notification to RocketMQ: id={}", notification.getId(), e);
            notification.setStatus("FAILED");
            notificationRepository.updateById(notification);
        }
    }

    public NotificationResponse getById(Long id) {
        Notification notification = notificationRepository.selectById(id);
        if (notification == null) {
            throw new BusinessException(3001, "通知不存在");
        }
        return toResponse(notification);
    }

    public IPage<NotificationResponse> query(NotificationQueryRequest request) {
        Page<Notification> page = new Page<>(request.getPage(), request.getPageSize());

        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        if (request.getUserId() != null) {
            wrapper.eq(Notification::getUserId, request.getUserId());
        }
        if (request.getType() != null) {
            wrapper.eq(Notification::getType, request.getType());
        }
        if (request.getChannel() != null) {
            wrapper.eq(Notification::getChannel, request.getChannel());
        }
        if (request.getStatus() != null) {
            wrapper.eq(Notification::getStatus, request.getStatus());
        }

        wrapper.orderByDesc(Notification::getCreatedAt);

        IPage<Notification> result = notificationRepository.selectPage(page, wrapper);

        return result.convert(this::toResponse);
    }

    public void delete(Long id) {
        Notification notification = notificationRepository.selectById(id);
        if (notification == null) {
            throw new BusinessException(3001, "通知不存在");
        }
        notificationRepository.deleteById(id);
    }

    private NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .title(notification.getTitle())
                .content(notification.getContent())
                .channel(notification.getChannel())
                .status(notification.getStatus())
                .receiver(notification.getReceiver())
                .createdAt(notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null)
                .build();
    }
}
