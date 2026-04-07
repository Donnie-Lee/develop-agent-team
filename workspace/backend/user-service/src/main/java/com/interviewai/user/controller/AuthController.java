package com.interviewai.user.controller;

import com.interviewai.common.Result;
import com.interviewai.user.dto.SendCodeRequest;
import com.interviewai.user.dto.PhoneLoginRequest;
import com.interviewai.user.dto.LoginResponse;
import com.interviewai.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/send-code")
    public Result<Map<String, Integer>> sendCode(@Valid @RequestBody SendCodeRequest request) {
        int expiresIn = authService.sendCode(request);
        return Result.success(Map.of("expiresIn", expiresIn));
    }

    @PostMapping("/login-phone")
    public Result<LoginResponse> phoneLogin(@Valid @RequestBody PhoneLoginRequest request) {
        LoginResponse response = authService.phoneLogin(request);
        return Result.success(response);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return Result.success();
    }

    @PostMapping("/refresh-token")
    public Result<LoginResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        LoginResponse response = authService.refreshToken(refreshToken);
        return Result.success(response);
    }
}
