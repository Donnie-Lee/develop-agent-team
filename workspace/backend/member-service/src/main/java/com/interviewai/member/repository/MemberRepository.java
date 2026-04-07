package com.interviewai.member.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.member.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberRepository extends BaseMapper<Member> {
}
