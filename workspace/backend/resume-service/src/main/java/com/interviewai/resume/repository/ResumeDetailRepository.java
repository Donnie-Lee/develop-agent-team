package com.interviewai.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.resume.entity.ResumeDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeDetailRepository extends BaseMapper<ResumeDetail> {
}
