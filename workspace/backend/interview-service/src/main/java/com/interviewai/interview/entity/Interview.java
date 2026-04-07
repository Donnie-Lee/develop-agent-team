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
@TableName("interviews")
public class Interview {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String interviewType;

    private String position;

    private Integer duration;

    private String status;

    private Integer totalScore;

    private Integer questionCount;

    private Integer answeredCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String summary;

    private String feedback;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    @TableLogic
    private LocalDateTime deletedAt;
}
