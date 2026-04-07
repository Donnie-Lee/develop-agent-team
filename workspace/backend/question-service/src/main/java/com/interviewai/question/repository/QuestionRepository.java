package com.interviewai.question.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.question.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionRepository extends BaseMapper<Question> {
}