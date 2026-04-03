package com.interviewai.resume.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.common.BusinessException;
import com.interviewai.resume.entity.Resume;
import com.interviewai.resume.repository.ResumeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AIParseService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String PARSE_STATUS_PREFIX = "resume:parse:";

    public void startParse(Long resumeId, String fileUrl) {
        // 更新状态为解析中
        Resume resume = resumeRepository.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        resume.setParseStatus(1); // 解析中
        resumeRepository.updateById(resume);

        // 存储解析任务到Redis
        String key = PARSE_STATUS_PREFIX + resumeId;
        redisTemplate.opsForValue().set(key, "processing", Duration.ofMinutes(5));

        // TODO: 实际调用AI服务解析简历
        // 这里简化处理，实际生产环境需要调用AI网关
        // 使用异步方式处理
        asyncParseResume(resumeId, fileUrl);
    }

    private void asyncParseResume(Long resumeId, String fileUrl) {
        // 异步处理
        new Thread(() -> {
            try {
                // 模拟AI解析过程
                Thread.sleep(3000);

                // 构造模拟解析结果
                Map<String, Object> parseResult = new HashMap<>();
                parseResult.put("name", "张三");
                parseResult.put("phone", "13800138000");
                parseResult.put("email", "zhangsan@example.com");
                parseResult.put("education", "本科 - 计算机科学与技术 - 清华大学");
                parseResult.put("experience", "5年开发经验");
                parseResult.put("skills", new String[]{"Java", "Spring Boot", "MySQL", "Redis", "微服务"});

                String resultJson;
                try {
                    resultJson = objectMapper.writeValueAsString(parseResult);
                } catch (Exception e) {
                    resultJson = "{}";
                }

                // 更新简历解析结果
                Resume resume = resumeRepository.selectById(resumeId);
                if (resume != null) {
                    resume.setParseStatus(2); // 已解析
                    resume.setParseResult(resultJson);
                    resumeRepository.updateById(resume);
                }

                // 删除Redis中的处理状态
                String key = PARSE_STATUS_PREFIX + resumeId;
                redisTemplate.delete(key);

                log.info("简历解析完成: resumeId={}", resumeId);
            } catch (Exception e) {
                log.error("简历解析失败: resumeId={}, error={}", resumeId, e.getMessage(), e);

                // 更新状态为失败
                Resume resume = resumeRepository.selectById(resumeId);
                if (resume != null) {
                    resume.setParseStatus(0); // 重置为未解析
                    resumeRepository.updateById(resume);
                }
            }
        }).start();
    }

    public Map<String, Object> getParseResult(Long resumeId) {
        Resume resume = resumeRepository.selectById(resumeId);
        if (resume == null) {
            throw new BusinessException(3001, "简历不存在");
        }

        if (resume.getParseResult() == null) {
            return null;
        }

        try {
            return objectMapper.readValue(resume.getParseResult(), Map.class);
        } catch (Exception e) {
            log.error("解析简历结果失败: {}", e.getMessage(), e);
            return null;
        }
    }

    public boolean isParseCompleted(Long resumeId) {
        String key = PARSE_STATUS_PREFIX + resumeId;
        return !redisTemplate.hasKey(key);
    }
}
