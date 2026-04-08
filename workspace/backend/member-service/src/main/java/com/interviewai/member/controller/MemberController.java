package com.interviewai.member.controller;

import com.interviewai.common.BusinessException;
import com.interviewai.common.Result;
import com.interviewai.member.dto.CreateMemberRequest;
import com.interviewai.member.dto.MemberResponse;
import com.interviewai.member.dto.UpdateMemberRequest;
import com.interviewai.member.service.MemberService;
import com.interviewai.member.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public Result<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return Result.success(response);
    }

    @GetMapping("/{id}")
    public Result<MemberResponse> getMemberById(@PathVariable("id") Long id) {
        MemberResponse response = memberService.getMemberById(id);
        return Result.success(response);
    }

    @GetMapping("/user/{userId}")
    public Result<MemberResponse> getMemberByUserId(@PathVariable("userId") Long userId) {
        MemberResponse response = memberService.getMemberByUserId(userId);
        return Result.success(response);
    }

    @GetMapping("/list")
    public Result<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> response = memberService.getAllMembers();
        return Result.success(response);
    }

    @PutMapping("/{id}")
    public Result<MemberResponse> updateMember(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateMemberRequest request) {
        MemberResponse response = memberService.updateMember(id, request);
        return Result.success(response);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return Result.success();
    }

    @PostMapping("/{id}/renew")
    public Result<MemberResponse> renewMember(
            @PathVariable("id") Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expireTime) {
        MemberResponse response = memberService.renewMember(id, expireTime);
        return Result.success(response);
    }

    @GetMapping("/info")
    public Result<MemberResponse> getCurrentMemberInfo(@RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromToken(authHeader);
        MemberResponse response = memberService.getMemberByUserId(userId);
        return Result.success(response);
    }

    private Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(2001, "Token无效");
        }
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }
}
