package com.interviewai.interview.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("interview_questions")
public class InterviewQuestion {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long interviewId;

    private Long sessionId;

    private String questionType;

    private String questionText;

    private String expectedAnswer;

    private String userAnswer;

    private Integer score;

    private String feedback;

    private Integer orderIndex;

    private String status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    @TableLogic
    private LocalDateTime deletedAt;
}
