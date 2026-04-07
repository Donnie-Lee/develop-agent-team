package com.interviewai.resume.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.common.BusinessException;
import com.interviewai.resume.dto.*;
import com.interviewai.resume.entity.Resume;
import com.interviewai.resume.entity.ResumeDetail;
import com.interviewai.resume.repository.ResumeDetailRepository;
import com.interviewai.resume.repository.ResumeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeDetailRepository resumeDetailRepository;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private AIParseService aiParseService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ResumeListResponse> getResumeList(Long userId) {
        List<Resume> resumes = resumeRepository.selectList(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
                        .orderByDesc(Resume::getUpdatedAt)
        );

        return resumes.stream().map(resume -> ResumeListResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .isDefault(resume.getIsDefault() == 1)
                .updatedAt(resume.getUpdatedAt() != null ? resume.getUpdatedAt().toString() : null)
                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ResumeDetailResponse createResume(Long userId, CreateResumeRequest request) {
        // 检查是否是第一个简历，如果是则设为默认
        long count = resumeRepository.selectCount(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setTitle(request.getTitle());
        resume.setIsDefault(count == 0 ? 1 : 0); // 第一个设为默认
        resume.setParseStatus(0); // 未解析
        resume.setCreatedAt(LocalDateTime.now());
        resume.setUpdatedAt(LocalDateTime.now());
        resumeRepository.insert(resume);

        return ResumeDetailResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .isDefault(resume.getIsDefault() == 1)
                .parseStatus(resume.getParseStatus())
                .sections(new ArrayList<>())
                .updatedAt(resume.getUpdatedAt().toString())
                .build();
    }

    public ResumeDetailResponse getResumeDetail(Long userId, Long resumeId) {
        Resume resume = resumeRepository.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getId, resumeId)
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        // 获取简历详情
        List<ResumeDetail> details = resumeDetailRepository.selectList(
                new LambdaQueryWrapper<ResumeDetail>()
                        .eq(ResumeDetail::getResumeId, resumeId)
                        .orderByAsc(ResumeDetail::getSortOrder)
        );

        List<ResumeSectionDTO> sections = details.stream().map(detail -> {
            Object content = null;
            if (detail.getContent() != null) {
                try {
                    content = objectMapper.readValue(detail.getContent(), Object.class);
                } catch (JsonProcessingException e) {
                    log.error("解析简历详情失败: {}", e.getMessage());
                }
            }
            return ResumeSectionDTO.builder()
                    .sectionType(detail.getSectionType())
                    .content(content)
                    .sortOrder(detail.getSortOrder())
                    .build();
        }).collect(Collectors.toList());

        Object parseResult = null;
        if (resume.getParseResult() != null) {
            try {
                parseResult = objectMapper.readValue(resume.getParseResult(), Object.class);
            } catch (JsonProcessingException e) {
                log.error("解析简历结果失败: {}", e.getMessage());
            }
        }

        return ResumeDetailResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .isDefault(resume.getIsDefault() == 1)
                .parseStatus(resume.getParseStatus())
                .parseResult(parseResult)
                .sections(sections)
                .updatedAt(resume.getUpdatedAt() != null ? resume.getUpdatedAt().toString() : null)
                .build();
    }

    @Transactional
    public ResumeDetailResponse updateResume(Long userId, Long resumeId, UpdateResumeRequest request) {
        Resume resume = resumeRepository.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getId, resumeId)
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        // 更新简历基本信息
        if (request.getTitle() != null) {
            resume.setTitle(request.getTitle());
        }
        resume.setUpdatedAt(LocalDateTime.now());
        resumeRepository.updateById(resume);

        // 更新简历详情
        if (request.getSections() != null && !request.getSections().isEmpty()) {
            // 删除旧的详情
            resumeDetailRepository.delete(
                    new LambdaQueryWrapper<ResumeDetail>()
                            .eq(ResumeDetail::getResumeId, resumeId)
            );

            // 插入新的详情
            for (int i = 0; i < request.getSections().size(); i++) {
                ResumeSectionDTO section = request.getSections().get(i);
                ResumeDetail detail = new ResumeDetail();
                detail.setResumeId(resumeId);
                detail.setSectionType(section.getSectionType());
                detail.setSortOrder(section.getSortOrder() != null ? section.getSortOrder() : i);
                try {
                    detail.setContent(objectMapper.writeValueAsString(section.getContent()));
                } catch (JsonProcessingException e) {
                    detail.setContent("{}");
                }
                detail.setCreatedAt(LocalDateTime.now());
                detail.setUpdatedAt(LocalDateTime.now());
                resumeDetailRepository.insert(detail);
            }
        }

        return getResumeDetail(userId, resumeId);
    }

    @Transactional
    public void deleteResume(Long userId, Long resumeId) {
        Resume resume = resumeRepository.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getId, resumeId)
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        // 软删除
        resume.setDeletedAt(LocalDateTime.now());
        resumeRepository.updateById(resume);

        // 删除关联的详情
        resumeDetailRepository.delete(
                new LambdaQueryWrapper<ResumeDetail>()
                        .eq(ResumeDetail::getResumeId, resumeId)
        );
    }

    public UploadResponse uploadFile(Long userId, Long resumeId, String fileUrl) {
        Resume resume;
        if (resumeId != null) {
            resume = resumeRepository.selectOne(
                    new LambdaQueryWrapper<Resume>()
                            .eq(Resume::getId, resumeId)
                            .eq(Resume::getUserId, userId)
                            .isNull(Resume::getDeletedAt)
            );
            if (resume == null) {
                throw new BusinessException(3001, "简历不存在");
            }
        } else {
            // 创建新简历
            resume = new Resume();
            resume.setUserId(userId);
            resume.setTitle("我的简历");
            resume.setIsDefault(1);
            resume.setParseStatus(0);
            resume.setCreatedAt(LocalDateTime.now());
            resume.setUpdatedAt(LocalDateTime.now());
            resumeRepository.insert(resume);
        }

        // 更新文件URL
        resume.setFileUrl(fileUrl);
        resume.setUpdatedAt(LocalDateTime.now());
        resumeRepository.updateById(resume);

        return UploadResponse.builder()
                .fileUrl(fileUrl)
                .fileName(fileUrl.substring(fileUrl.lastIndexOf("/") + 1))
                .build();
    }

    public ParseResponse parseResume(Long userId, Long resumeId) {
        Resume resume = resumeRepository.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getId, resumeId)
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        if (resume.getFileUrl() == null) {
            throw new BusinessException(3001, "请先上传简历文件");
        }

        // 启动异步解析
        aiParseService.startParse(resumeId, resume.getFileUrl());

        return ParseResponse.builder()
                .parseStatus(0) // 进行中
                .parseResult(null)
                .build();
    }

    public AnalyzeResponse analyzeResume(Long userId, Long resumeId) {
        Resume resume = resumeRepository.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getId, resumeId)
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        if (resume.getParseStatus() != 2) {
            throw new BusinessException(3003, "简历尚未解析完成");
        }

        // 获取解析结果
        Map<String, Object> parseResult = aiParseService.getParseResult(resumeId);
        if (parseResult == null) {
            throw new BusinessException(3001, "解析结果不存在");
        }

        // TODO: 调用AI服务进行深入分析
        // 这里返回模拟数据
        List<String> keywords = new ArrayList<>();
        keywords.add("Java");
        keywords.add("Spring Boot");
        keywords.add("微服务");

        List<String> highlights = new ArrayList<>();
        highlights.add("知名互联网公司工作经验");
        highlights.add("完整项目开发经验");

        List<String> risks = new ArrayList<>();
        risks.add("简历内容较少，建议补充更多项目经验");

        return AnalyzeResponse.builder()
                .completeness(75)
                .keywords(keywords)
                .competitiveness(72)
                .highlights(highlights)
                .risks(risks)
                .build();
    }

    public SuggestionsResponse getSuggestions(Long userId, Long resumeId) {
        Resume resume = resumeRepository.selectOne(
                new LambdaQueryWrapper<Resume>()
                        .eq(Resume::getId, resumeId)
                        .eq(Resume::getUserId, userId)
                        .isNull(Resume::getDeletedAt)
        );

        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        // TODO: 调用AI服务获取优化建议
        // 这里返回模拟数据
        List<String> contentSuggestions = new ArrayList<>();
        contentSuggestions.add("建议补充具体的工作成果和数据");
        contentSuggestions.add("完善项目经验的描述");

        List<String> structureSuggestions = new ArrayList<>();
        structureSuggestions.add("建议按照时间倒序排列工作经历");
        structureSuggestions.add("确保信息层次清晰");

        List<String> keywordSuggestions = new ArrayList<>();
        keywordSuggestions.add("建议增加: 团队协作、项目管理");
        keywordSuggestions.add("可以突出: 架构设计、性能优化");

        return SuggestionsResponse.builder()
                .contentSuggestions(contentSuggestions)
                .structureSuggestions(structureSuggestions)
                .keywordSuggestions(keywordSuggestions)
                .build();
    }
}
