package com.interviewai.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.interviewai.common.BusinessException;
import com.interviewai.user.dto.UpdateUserInfoRequest;
import com.interviewai.user.dto.UserInfoResponse;
import com.interviewai.user.dto.ChangePasswordRequest;
import com.interviewai.user.entity.User;
import com.interviewai.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(3001, "用户不存在");
        }

        return UserInfoResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .memberLevel(user.getMemberLevel())
                .memberExpire(user.getMemberExpire() != null ? user.getMemberExpire().toString() : null)
                .build();
    }

    public UserInfoResponse updateUserInfo(Long userId, UpdateUserInfoRequest request) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(3001, "用户不存在");
        }

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }

        userRepository.updateById(user);

        return UserInfoResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .memberLevel(user.getMemberLevel())
                .memberExpire(user.getMemberExpire() != null ? user.getMemberExpire().toString() : null)
                .build();
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException(3001, "用户不存在");
        }

        // 如果用户设置了密码，验证原密码
        if (user.getPasswordHash() != null) {
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
                throw new BusinessException(2003, "原密码错误");
            }
        }

        // 更新为新密码
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.updateById(user);
    }
}
