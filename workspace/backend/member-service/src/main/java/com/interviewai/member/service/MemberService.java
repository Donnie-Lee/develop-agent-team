package com.interviewai.member.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.interviewai.common.BusinessException;
import com.interviewai.member.dto.CreateMemberRequest;
import com.interviewai.member.dto.MemberResponse;
import com.interviewai.member.dto.UpdateMemberRequest;
import com.interviewai.member.entity.Member;
import com.interviewai.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberResponse createMember(CreateMemberRequest request) {
        // 检查用户是否已经是会员
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getUserId, request.getUserId());
        Member existingMember = memberRepository.selectOne(queryWrapper);
        if (existingMember != null) {
            throw new BusinessException(3001, "用户已经是会员");
        }

        Member member = new Member();
        member.setUserId(request.getUserId());
        member.setLevel(request.getLevel() != null ? request.getLevel() : 1);
        member.setStatus(1); // 正常状态
        member.setMemberSince(LocalDateTime.now());
        member.setDescription(request.getDescription());

        memberRepository.insert(member);

        return toResponse(member);
    }

    public MemberResponse getMemberById(Long id) {
        Member member = memberRepository.selectById(id);
        if (member == null) {
            throw new BusinessException(3001, "会员不存在");
        }
        return toResponse(member);
    }

    public MemberResponse getMemberByUserId(Long userId) {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getUserId, userId);
        Member member = memberRepository.selectOne(queryWrapper);
        if (member == null) {
            throw new BusinessException(3001, "会员不存在");
        }
        return toResponse(member);
    }

    public List<MemberResponse> getAllMembers() {
        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Member::getStatus, 1);
        List<Member> members = memberRepository.selectList(queryWrapper);
        return members.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public MemberResponse updateMember(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.selectById(id);
        if (member == null) {
            throw new BusinessException(3001, "会员不存在");
        }

        if (request.getLevel() != null) {
            member.setLevel(request.getLevel());
        }
        if (request.getDescription() != null) {
            member.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            member.setStatus(request.getStatus());
        }
        if (request.getTotalAmount() != null) {
            member.setTotalAmount(request.getTotalAmount());
        }

        memberRepository.updateById(member);
        return toResponse(member);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.selectById(id);
        if (member == null) {
            throw new BusinessException(3001, "会员不存在");
        }
        memberRepository.deleteById(id);
    }

    public MemberResponse renewMember(Long id, LocalDateTime expireTime) {
        Member member = memberRepository.selectById(id);
        if (member == null) {
            throw new BusinessException(3001, "会员不存在");
        }
        member.setExpireTime(expireTime);
        memberRepository.updateById(member);
        return toResponse(member);
    }

    private MemberResponse toResponse(Member member) {
        String statusName = getStatusName(member.getStatus());
        return MemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .level(member.getLevel())
                .memberSince(member.getMemberSince())
                .expireTime(member.getExpireTime())
                .status(member.getStatus())
                .totalAmount(member.getTotalAmount())
                .description(member.getDescription())
                .statusName(statusName)
                .build();
    }

    private String getStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "正常";
            case 2:
                return "冻结";
            case 3:
                return "过期";
            default:
                return "未知";
        }
    }
}
