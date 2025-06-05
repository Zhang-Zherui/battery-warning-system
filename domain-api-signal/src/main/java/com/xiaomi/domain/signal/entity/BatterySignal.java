package com.xiaomi.domain.signal.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("battery_signal")
public class BatterySignal implements Serializable {
    @TableId
    private Integer carId;
    private Integer warnId;
    private Double max;
    private Double min;
}

