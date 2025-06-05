package com.xiaomi.domain.rule.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("warning_rule")
public class WarningRule {
    @TableId
    private Integer id;
    private Integer warnType;
    private String batteryType;
    private Double rangeStart;
    private Double rangeEnd;
    private Integer alertLevel;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
