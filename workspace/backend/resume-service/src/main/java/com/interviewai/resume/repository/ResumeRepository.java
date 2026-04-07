package com.interviewai.resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.interviewai.resume.entity.Resume;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeRepository extends BaseMapper<Resume> {
}
