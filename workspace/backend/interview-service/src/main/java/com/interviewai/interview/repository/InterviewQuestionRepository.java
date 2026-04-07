package com.interviewai.interview.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.interview.entity.InterviewQuestion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterviewQuestionRepository extends BaseMapper<InterviewQuestion> {
}
