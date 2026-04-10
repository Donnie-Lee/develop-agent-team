package com.interviewai.question.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.interviewai.common.BusinessException;
import com.interviewai.question.dto.CreateQuestionRequest;
import com.interviewai.question.dto.QuestionResponse;
import com.interviewai.question.dto.SearchQuestionRequest;
import com.interviewai.question.dto.UpdateQuestionRequest;
import com.interviewai.question.entity.Question;
import com.interviewai.question.repository.QuestionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionResponse createQuestion(CreateQuestionRequest request) {
        Question question = Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .type(request.getType())
                .difficulty(request.getDifficulty())
                .category(request.getCategory())
                .tags(request.getTags())
                .answer(request.getAnswer())
                .explanation(request.getExplanation())
                .viewCount(0)
                .likeCount(0)
                .collectCount(0)
                .build();

        questionRepository.insert(question);

        return toResponse(question);
    }

    public QuestionResponse updateQuestion(Long id, UpdateQuestionRequest request) {
        Question question = questionRepository.selectById(id);
        if (question == null) {
            throw new BusinessException(4001, "题目不存在");
        }

        if (StringUtils.hasText(request.getTitle())) {
            question.setTitle(request.getTitle());
        }
        if (StringUtils.hasText(request.getContent())) {
            question.setContent(request.getContent());
        }
        if (StringUtils.hasText(request.getType())) {
            question.setType(request.getType());
        }
        if (StringUtils.hasText(request.getDifficulty())) {
            question.setDifficulty(request.getDifficulty());
        }
        if (StringUtils.hasText(request.getCategory())) {
            question.setCategory(request.getCategory());
        }
        if (request.getTags() != null) {
            question.setTags(request.getTags());
        }
        if (request.getAnswer() != null) {
            question.setAnswer(request.getAnswer());
        }
        if (request.getExplanation() != null) {
            question.setExplanation(request.getExplanation());
        }

        questionRepository.updateById(question);

        return toResponse(question);
    }

    public QuestionResponse getQuestion(Long id) {
        Question question = questionRepository.selectById(id);
        if (question == null) {
            throw new BusinessException(4001, "题目不存在");
        }

        // 增加浏览次数
        question.setViewCount(question.getViewCount() + 1);
        questionRepository.updateById(question);

        return toResponse(question);
    }

    public void deleteQuestion(Long id) {
        Question question = questionRepository.selectById(id);
        if (question == null) {
            throw new BusinessException(4001, "题目不存在");
        }

        questionRepository.deleteById(id);
    }

    public IPage<QuestionResponse> listQuestions(Integer page, Integer pageSize) {
        Page<Question> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Question::getCreatedAt);

        Page<Question> result = questionRepository.selectPage(pageParam, wrapper);

        // Map records and return Page with total info preserved
        List<QuestionResponse> records = result.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        Page<QuestionResponse> questionResponsePage = new Page<>();
        BeanUtils.copyProperties(result, questionResponsePage);
        questionResponsePage.setRecords(records);
        return questionResponsePage;
    }

    public IPage<QuestionResponse> searchQuestions(SearchQuestionRequest request) {
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(request.getKeyword())) {
            wrapper.and(w -> w.like(Question::getTitle, request.getKeyword())
                    .or()
                    .like(Question::getContent, request.getKeyword()));
        }

        if (StringUtils.hasText(request.getType())) {
            wrapper.eq(Question::getType, request.getType());
        }

        if (StringUtils.hasText(request.getDifficulty())) {
            wrapper.eq(Question::getDifficulty, request.getDifficulty());
        }

        if (StringUtils.hasText(request.getCategory())) {
            wrapper.eq(Question::getCategory, request.getCategory());
        }

        wrapper.orderByDesc(Question::getCreatedAt);

        Page<Question> pageParam = new Page<>(request.getPage(), request.getPageSize());
        Page<Question> result = questionRepository.selectPage(pageParam, wrapper);

        // Map records and return Page with total info preserved
        List<QuestionResponse> records = result.getRecords().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        Page<QuestionResponse> questionResponsePage = new Page<>();
        BeanUtils.copyProperties(result, questionResponsePage);
        questionResponsePage.setRecords(records);
        return questionResponsePage;
    }

    public Long getCount() {
        try {
            Long count = questionRepository.selectCount(null);
            return count != null ? count : 0L;
        } catch (Exception e) {
            log.error("Failed to get question count", e);
            return 0L;
        }
    }

    private QuestionResponse toResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .type(question.getType())
                .difficulty(question.getDifficulty())
                .category(question.getCategory())
                .tags(question.getTags())
                .answer(question.getAnswer())
                .explanation(question.getExplanation())
                .viewCount(question.getViewCount())
                .likeCount(question.getLikeCount())
                .collectCount(question.getCollectCount())
                .createdAt(question.getCreatedAt() != null ? question.getCreatedAt().toString() : null)
                .updatedAt(question.getUpdatedAt() != null ? question.getUpdatedAt().toString() : null)
                .build();
    }
}
