package com.interviewai.interview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interviewai.common.BusinessException;
import com.interviewai.interview.entity.Interview;
import com.interviewai.interview.entity.InterviewQuestion;
import com.interviewai.interview.entity.InterviewSession;
import com.interviewai.interview.repository.InterviewQuestionRepository;
import com.interviewai.interview.repository.InterviewRepository;
import com.interviewai.interview.repository.InterviewSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AiQuestionService {

    @Autowired
    private InterviewRepository interviewRepository;

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private InterviewQuestionRepository questionRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String AI_GATEWAY_URL = "http://localhost:8083/api/v1/ai/chat";
    private static final String CONVERSATION_HISTORY_PREFIX = "interview:history:";

    /**
     * Generate interview questions using AI
     */
    @Transactional
    public List<InterviewQuestion> generateQuestions(Long interviewId, Long userId, String position, String interviewType) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }

        InterviewSession session = sessionRepository.selectById(interview.getId());
        if (session == null) {
            throw new BusinessException(3001, "会话不存在");
        }

        // Build AI prompt for question generation
        String prompt = buildQuestionPrompt(position, interviewType);

        // Call AI Gateway
        String aiResponse = callAiGateway(prompt, userId);

        // Parse AI response and create questions
        List<InterviewQuestion> questions = parseAndCreateQuestions(aiResponse, interviewId, session.getId(), interviewType);

        // Update interview question count
        interview.setQuestionCount(questions.size());
        interviewRepository.updateById(interview);

        return questions;
    }

    /**
     * Generate follow-up question based on previous answer
     */
    @Transactional
    public InterviewQuestion generateFollowUpQuestion(Long interviewId, Long sessionId, Long userId,
                                                       String previousQuestion, String previousAnswer) {
        InterviewSession session = sessionRepository.selectById(sessionId);
        if (session == null) {
            throw new BusinessException(3001, "会话不存在");
        }

        // Build prompt for follow-up question
        String prompt = buildFollowUpPrompt(previousQuestion, previousAnswer);

        // Call AI Gateway
        String aiResponse = callAiGateway(prompt, userId);

        // Parse response and create follow-up question
        return parseAndCreateFollowUpQuestion(aiResponse, interviewId, sessionId, session.getCurrentQuestionIndex() + 1);
    }

    /**
     * Analyze answer and provide feedback
     */
    public Map<String, Object> analyzeAnswer(String question, String answer) {
        Map<String, Object> result = new HashMap<>();

        // Build prompt for answer analysis
        String prompt = buildAnswerAnalysisPrompt(question, answer);

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", prompt);
            requestBody.put("temperature", 0.5);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(AI_GATEWAY_URL, entity, String.class);

            // Parse response
            if (response != null) {
                JsonNode root = objectMapper.readTree(response);
                if (root.has("data") && root.get("data").has("content")) {
                    String analysis = root.get("data").get("content").asText();
                    result.put("analysis", analysis);
                    result.put("score", calculateScore(analysis));
                    result.put("feedback", extractFeedback(analysis));
                }
            }
        } catch (Exception e) {
            log.error("Failed to analyze answer", e);
            result.put("analysis", "答案已记录");
            result.put("score", 0);
            result.put("feedback", "");
        }

        return result;
    }

    /**
     * Generate interview report
     */
    public Map<String, Object> generateReport(Long interviewId) {
        Interview interview = interviewRepository.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(3001, "面试记录不存在");
        }

        // Get all questions and answers
        List<InterviewQuestion> questions = questionRepository.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InterviewQuestion>()
                        .eq(InterviewQuestion::getInterviewId, interviewId)
                        .orderByAsc(InterviewQuestion::getOrderIndex)
        );

        // Build report prompt
        String prompt = buildReportPrompt(interview, questions);

        // Call AI Gateway
        String aiResponse = callAiGateway(prompt, interview.getUserId());

        // Parse and build report
        return parseReport(aiResponse, interview, questions);
    }

    private String buildQuestionPrompt(String position, String interviewType) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的面试官。请为应聘").append(position).append("岗位的候选人生成面试问题。\n\n");
        prompt.append("面试类型: ").append(interviewType).append("\n\n");

        if ("technical".equals(interviewType)) {
            prompt.append("请生成5个技术问题，包括:\n");
            prompt.append("1. 基础知识问题\n");
            prompt.append("2. 框架使用问题\n");
            prompt.append("3. 性能优化问题\n");
            prompt.append("4. 实际场景问题\n");
            prompt.append("5. 扩展性问题\n\n");
        } else if ("behavioral".equals(interviewType)) {
            prompt.append("请生成5个行为面试问题，包括:\n");
            prompt.append("1. 团队协作问题\n");
            prompt.append("2. 解决问题能力\n");
            prompt.append("3. 抗压能力\n");
            prompt.append("4. 职业规划\n");
            prompt.append("5. 价值观匹配\n\n");
        } else {
            prompt.append("请生成5个综合面试问题:\n");
        }

        prompt.append("请以JSON格式返回，格式如下:\n");
        prompt.append("{\"questions\": [{\"type\": \"技术基础\", \"question\": \"问题内容\"}]}");

        return prompt.toString();
    }

    private String buildFollowUpPrompt(String previousQuestion, String previousAnswer) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("基于以下面试对话，生成一个追问问题。\n\n");
        prompt.append("面试官问题: ").append(previousQuestion).append("\n\n");
        prompt.append("候选人回答: ").append(previousAnswer).append("\n\n");
        prompt.append("请根据候选人的回答，提出一个相关的追问。追问应该:\n");
        prompt.append("1. 深入探讨候选人回答中的某个细节\n");
        prompt.append("2. 或者让候选人举例说明\n");
        prompt.append("3. 保持专业且友好的语气\n\n");
        prompt.append("请只返回追问问题，不要其他内容。");

        return prompt.toString();
    }

    private String buildAnswerAnalysisPrompt(String question, String answer) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请分析以下面试回答:\n\n");
        prompt.append("问题: ").append(question).append("\n\n");
        prompt.append("回答: ").append(answer).append("\n\n");
        prompt.append("请从以下几个方面给出简短评价:\n");
        prompt.append("1. 回答的完整性\n");
        prompt.append("2. 技术准确性\n");
        prompt.append("3. 表达清晰度\n");
        prompt.append("4. 建议改进点\n\n");
        prompt.append("请用简洁的语言回复，控制在100字以内。");

        return prompt.toString();
    }

    private String buildReportPrompt(Interview interview, List<InterviewQuestion> questions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("请根据以下面试记录生成面试评估报告:\n\n");
        prompt.append("面试岗位: ").append(interview.getPosition()).append("\n");
        prompt.append("面试类型: ").append(interview.getInterviewType()).append("\n");
        prompt.append("问题数量: ").append(questions.size()).append("\n\n");

        for (int i = 0; i < questions.size(); i++) {
            InterviewQuestion q = questions.get(i);
            prompt.append("问题").append(i + 1).append(": ").append(q.getQuestionText()).append("\n");
            if (q.getUserAnswer() != null) {
                prompt.append("回答: ").append(q.getUserAnswer()).append("\n");
            }
            if (q.getFeedback() != null) {
                prompt.append("评价: ").append(q.getFeedback()).append("\n");
            }
            prompt.append("\n");
        }

        prompt.append("\n请生成JSON格式的评估报告:\n");
        prompt.append("{\n");
        prompt.append("  \"summary\": \"总体评价\",\n");
        prompt.append("  \"strengths\": [\"优点1\", \"优点2\"],\n");
        prompt.append("  \"weaknesses\": [\"不足1\", \"不足2\"],\n");
        prompt.append("  \"recommendation\": \"录用建议\",\n");
        prompt.append("  \"overallScore\": 85\n");
        prompt.append("}");

        return prompt.toString();
    }

    private String callAiGateway(String prompt, Long userId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("message", prompt);
            requestBody.put("temperature", 0.7);
            requestBody.put("userId", userId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            String response = restTemplate.postForObject(AI_GATEWAY_URL, entity, String.class);

            // Parse response to extract content
            if (response != null) {
                JsonNode root = objectMapper.readTree(response);
                if (root.has("data") && root.get("data").has("content")) {
                    return root.get("data").get("content").asText();
                }
            }
            return "";
        } catch (Exception e) {
            log.error("Failed to call AI Gateway", e);
            throw new BusinessException(4001, "AI服务调用失败: " + e.getMessage());
        }
    }

    private List<InterviewQuestion> parseAndCreateQuestions(String aiResponse, Long interviewId, Long sessionId, String interviewType) {
        List<InterviewQuestion> questions = new ArrayList<>();

        try {
            // Try to parse JSON response
            String jsonContent = extractJsonFromResponse(aiResponse);
            JsonNode root = objectMapper.readTree(jsonContent);

            JsonNode questionsNode = root.get("questions");
            if (questionsNode != null && questionsNode.isArray()) {
                int orderIndex = 0;
                for (JsonNode qNode : questionsNode) {
                    InterviewQuestion question = InterviewQuestion.builder()
                            .interviewId(interviewId)
                            .sessionId(sessionId)
                            .questionType(qNode.has("type") ? qNode.get("type").asText() : interviewType)
                            .questionText(qNode.has("question") ? qNode.get("question").asText() : qNode.asText())
                            .status("pending")
                            .orderIndex(orderIndex++)
                            .build();
                    questionRepository.insert(question);
                    questions.add(question);
                }
            }
        } catch (Exception e) {
            log.error("Failed to parse AI response, creating default questions", e);
            // Create default questions if parsing fails
            questions = createDefaultQuestions(interviewId, sessionId, interviewType);
        }

        // Ensure we have at least some questions
        if (questions.isEmpty()) {
            questions = createDefaultQuestions(interviewId, sessionId, interviewType);
        }

        return questions;
    }

    private InterviewQuestion parseAndCreateFollowUpQuestion(String aiResponse, Long interviewId, Long sessionId, int orderIndex) {
        String questionText = aiResponse.trim();

        // Remove quotes if present
        if (questionText.startsWith("\"") && questionText.endsWith("\"")) {
            questionText = questionText.substring(1, questionText.length() - 1);
        }

        InterviewQuestion question = InterviewQuestion.builder()
                .interviewId(interviewId)
                .sessionId(sessionId)
                .questionType("follow_up")
                .questionText(questionText)
                .status("pending")
                .orderIndex(orderIndex)
                .build();

        questionRepository.insert(question);
        return question;
    }

    private List<InterviewQuestion> createDefaultQuestions(Long interviewId, Long sessionId, String interviewType) {
        List<InterviewQuestion> questions = new ArrayList<>();

        List<String> defaultQuestions;
        if ("technical".equals(interviewType)) {
            defaultQuestions = Arrays.asList(
                    "请介绍一下你最近做的一个项目，以及你在其中承担的角色？",
                    "你在项目中遇到的最大技术挑战是什么？如何解决的？",
                    "请解释一下你熟悉的某个框架的工作原理？",
                    "如何优化一个慢查询？",
                    "请描述一下你对微服务架构的理解？"
            );
        } else if ("behavioral".equals(interviewType)) {
            defaultQuestions = Arrays.asList(
                    "请描述一次你在团队中解决冲突的经历？",
                    "你是如何处理工作中的压力的？",
                    "请分享一个你主动改进工作流程的例子？",
                    "你为什么想加入我们公司？",
                    "你未来5年的职业规划是什么？"
            );
        } else {
            defaultQuestions = Arrays.asList(
                    "请简单介绍一下你自己？",
                    "你最大的优点和缺点是什么？",
                    "为什么你想应聘这个职位？",
                    "你对薪资有什么期望？",
                    "你有什么问题想问我吗？"
            );
        }

        for (int i = 0; i < defaultQuestions.size(); i++) {
            InterviewQuestion question = InterviewQuestion.builder()
                    .interviewId(interviewId)
                    .sessionId(sessionId)
                    .questionType(interviewType)
                    .questionText(defaultQuestions.get(i))
                    .status("pending")
                    .orderIndex(i)
                    .build();
            questionRepository.insert(question);
            questions.add(question);
        }

        return questions;
    }

    private String extractJsonFromResponse(String response) {
        // Try to find JSON content in the response
        int startIndex = response.indexOf("{");
        int endIndex = response.lastIndexOf("}");
        if (startIndex >= 0 && endIndex > startIndex) {
            return response.substring(startIndex, endIndex + 1);
        }
        // If no JSON found, wrap the entire response
        return "{\"questions\": [{\"question\": \"" + response.replace("\"", "\\\"") + "\"}]}";
    }

    private int calculateScore(String analysis) {
        // Simple scoring based on response length and keywords
        int score = 70; // Base score
        if (analysis.length() > 50) score += 10;
        if (analysis.contains("优秀") || analysis.contains("good")) score += 10;
        if (analysis.contains("良好") || analysis.contains("well")) score += 5;
        if (analysis.contains("不足") || analysis.contains("建议")) score -= 10;
        return Math.min(100, Math.max(0, score));
    }

    private String extractFeedback(String analysis) {
        // Extract key feedback points
        if (analysis.length() > 100) {
            return analysis.substring(0, 100) + "...";
        }
        return analysis;
    }

    private Map<String, Object> parseReport(String aiResponse, Interview interview, List<InterviewQuestion> questions) {
        Map<String, Object> report = new HashMap<>();

        try {
            String jsonContent = extractJsonFromResponse(aiResponse);
            JsonNode root = objectMapper.readTree(jsonContent);

            report.put("summary", root.has("summary") ? root.get("summary").asText() : "面试完成");
            report.put("strengths", root.has("strengths") ? parseStringArray(root.get("strengths")) : Arrays.asList("表达清晰"));
            report.put("weaknesses", root.has("weaknesses") ? parseStringArray(root.get("weaknesses")) : Arrays.asList("可进一步丰富经验"));
            report.put("recommendation", root.has("recommendation") ? root.get("recommendation").asText() : "建议面试");
            report.put("overallScore", root.has("overallScore") ? root.get("overallScore").asInt() : calculateOverallScore(questions));
        } catch (Exception e) {
            log.error("Failed to parse report response", e);
            report.put("summary", "面试已完成，建议进一步面试以了解更多");
            report.put("strengths", Arrays.asList("态度积极"));
            report.put("weaknesses", Arrays.asList("经验尚浅"));
            report.put("recommendation", "建议考虑");
            report.put("overallScore", calculateOverallScore(questions));
        }

        report.put("interviewId", interview.getId());
        report.put("position", interview.getPosition());
        report.put("interviewType", interview.getInterviewType());
        report.put("questionCount", questions.size());
        report.put("answeredCount", questions.stream().filter(q -> "answered".equals(q.getStatus())).count());
        report.put("generatedAt", LocalDateTime.now().toString());

        return report;
    }

    private List<String> parseStringArray(JsonNode node) {
        List<String> result = new ArrayList<>();
        if (node != null && node.isArray()) {
            for (JsonNode item : node) {
                result.add(item.asText());
            }
        }
        return result;
    }

    private int calculateOverallScore(List<InterviewQuestion> questions) {
        if (questions.isEmpty()) return 0;

        int answeredCount = (int) questions.stream().filter(q -> q.getScore() != null).count();
        if (answeredCount == 0) return 70; // Default score if no scoring done

        int totalScore = questions.stream()
                .filter(q -> q.getScore() != null)
                .mapToInt(InterviewQuestion::getScore)
                .sum();

        return totalScore / answeredCount;
    }

    /**
     * Save conversation history for context
     */
    public void saveConversationHistory(Long sessionId, String role, String content) {
        String key = CONVERSATION_HISTORY_PREFIX + sessionId;
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        message.put("timestamp", LocalDateTime.now().toString());

        try {
            redisTemplate.opsForList().rightPush(key, objectMapper.writeValueAsString(message));
            // Keep only last 20 messages
            Long size = redisTemplate.opsForList().size(key);
            if (size != null && size > 20) {
                redisTemplate.opsForList().leftPop(key);
            }
        } catch (Exception e) {
            log.error("Failed to save conversation history", e);
        }
    }

    /**
     * Get conversation history
     */
    public List<Map<String, String>> getConversationHistory(Long sessionId) {
        String key = CONVERSATION_HISTORY_PREFIX + sessionId;
        List<String> messages = redisTemplate.opsForList().range(key, 0, -1);
        List<Map<String, String>> history = new ArrayList<>();

        if (messages != null) {
            for (String msg : messages) {
                try {
                    Map<String, String> message = objectMapper.readValue(msg, Map.class);
                    history.add(message);
                } catch (Exception e) {
                    log.error("Failed to parse conversation history", e);
                }
            }
        }

        return history;
    }
}
