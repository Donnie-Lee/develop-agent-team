package com.interviewai.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.interviewai.common.BusinessException;
import com.interviewai.user.config.JwtConfig;
import com.interviewai.user.dto.*;
import com.interviewai.user.entity.User;
import com.interviewai.user.repository.UserRepository;
import com.interviewai.user.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final int CODE_EXPIRE_SECONDS = 300;

    public int sendCode(SendCodeRequest request) {
        String phone = request.getPhone();
        String type = request.getType();

        // 生成6位验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 存储到Redis，5分钟过期
        String key = SMS_CODE_PREFIX + phone + ":" + type;
        redisTemplate.opsForValue().set(key, code, Duration.ofSeconds(CODE_EXPIRE_SECONDS));

        log.info("发送验证码: phone={}, type={}, code={}", phone, type, code);

        // TODO: 实际调用阿里云SMS服务发送验证码
        // 这里简化处理，实际生产环境需要调用SMS API

        return CODE_EXPIRE_SECONDS;
    }

    public LoginResponse phoneLogin(PhoneLoginRequest request) {
        String phone = request.getPhone();
        String code = request.getCode();

        // 验证验证码
        String key = SMS_CODE_PREFIX + phone + ":login";
        String storedCode = redisTemplate.opsForValue().get(key);

        if (!StringUtils.hasText(storedCode) || !storedCode.equals(code)) {
            throw new BusinessException(2001, "验证码错误或已过期");
        }

        // 删除已使用的验证码
        redisTemplate.delete(key);

        // 查询或创建用户
        User user = userRepository.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, phone)
        );

        if (user == null) {
            // 新用户注册
            user = new User();
            user.setPhone(phone);
            user.setNickname("用户" + phone.substring(phone.length() - 4));
            user.setMemberLevel(0);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.insert(user);
        }

        // 生成Token
        String accessToken = jwtUtil.generateAccessToken(user.getId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        // 构建用户信息
        UserInfoResponse userInfo = UserInfoResponse.builder()
                .id(user.getId())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .memberLevel(user.getMemberLevel())
                .memberExpire(user.getMemberExpire() != null ? user.getMemberExpire().toString() : null)
                .build();

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtConfig.getAccessTokenExpiration().intValue() / 1000)
                .userInfo(userInfo)
                .build();
    }

    public void logout(String accessToken) {
        // 将Token加入黑名单
        String key = "token:blacklist:" + accessToken;
        redisTemplate.opsForValue().set(key, "1",
                jwtConfig.getAccessTokenExpiration(), TimeUnit.MILLISECONDS);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(2002, "RefreshToken无效或已过期");
        }

        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        User user = userRepository.selectById(userId);

        if (user == null) {
            throw new BusinessException(3001, "用户不存在");
        }

        // 生成新的AccessToken
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtConfig.getAccessTokenExpiration().intValue() / 1000)
                .userInfo(UserInfoResponse.builder()
                        .id(user.getId())
                        .phone(user.getPhone())
                        .nickname(user.getNickname())
                        .avatarUrl(user.getAvatarUrl())
                        .memberLevel(user.getMemberLevel())
                        .memberExpire(user.getMemberExpire() != null ? user.getMemberExpire().toString() : null)
                        .build())
                .build();
    }
}
