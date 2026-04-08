package com.interviewai.member.entity;

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
@TableName("members")
public class Member {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer level;

    @TableField("member_since")
    private LocalDateTime memberSince;

    @TableField("expire_time")
    private LocalDateTime expireTime;

    private Integer status;

    @TableField("total_amount")
    private java.math.BigDecimal totalAmount;

    private String description;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField("deleted_at")
    @TableLogic(value = "null", delval = "now()")
    private LocalDateTime deletedAt;
}
