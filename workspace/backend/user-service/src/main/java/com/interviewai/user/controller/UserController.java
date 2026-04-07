package com.interviewai.user.controller;

import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.user.dto.UpdateUserInfoRequest;
import com.interviewai.user.dto.ChangePasswordRequest;
import com.interviewai.user.dto.UserInfoResponse;
import com.interviewai.user.service.UserService;
import com.interviewai.user.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        UserInfoResponse response = userService.getUserInfo(userId);
        return Result.success(response);
    }

    @PutMapping("/info")
    public Result<UserInfoResponse> updateUserInfo(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdateUserInfoRequest request) {
        Long userId = getUserIdFromToken(authHeader);
        UserInfoResponse response = userService.updateUserInfo(userId, request);
        return Result.success(response);
    }

    @PutMapping("/password")
    public Result<Void> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ChangePasswordRequest request) {
        Long userId = getUserIdFromToken(authHeader);
        userService.changePassword(userId, request);
        return Result.success();
    }

    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(2001, "Token无效");
        }
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }
}
