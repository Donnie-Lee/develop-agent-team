package com.interviewai.interview.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.interview.entity.InterviewSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterviewSessionRepository extends BaseMapper<InterviewSession> {
}
