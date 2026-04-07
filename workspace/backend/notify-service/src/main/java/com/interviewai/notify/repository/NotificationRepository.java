package com.interviewai.notify.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.notify.entity.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationRepository extends BaseMapper<Notification> {
}
