package com.interviewai.resume.entity;

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
@TableName("resumes")
public class Resume {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    private String title;

    @TableField("is_default")
    private Integer isDefault;

    @TableField("file_url")
    private String fileUrl;

    @TableField("parse_status")
    private Integer parseStatus; // 0: 未解析, 1: 解析中, 2: 已解析

    @TableField("parse_result")
    private String parseResult; // JSON

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    @TableLogic
    private LocalDateTime deletedAt;
}
