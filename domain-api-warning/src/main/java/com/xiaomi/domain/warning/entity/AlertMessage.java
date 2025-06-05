package com.xiaomi.domain.warning.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableField;

@Data
@TableName("alert_message")
public class AlertMessage {
    @TableField("car_id")
    private Integer carId;
    @TableField("warn_id")
    private Integer warnId;
    @TableField("alert_level")
    private Integer alertLevel;
    @TableField("battery_type")
    private String batteryType;
    @TableField("created_at")
    private Timestamp createdAt;
}

