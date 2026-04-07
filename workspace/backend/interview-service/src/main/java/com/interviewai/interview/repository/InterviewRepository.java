package com.interviewai.interview.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.interview.entity.Interview;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterviewRepository extends BaseMapper<Interview> {
}
