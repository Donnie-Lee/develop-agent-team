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
@TableName("interview_sessions")
public class InterviewSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long interviewId;

    private Long userId;

    private String sessionType;

    private String status;

    private String contextData;

    private Integer currentQuestionIndex;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    private LocalDateTime deletedAt;
}
