package com.xiaomi.domain.vehicle.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("battery_vehicle")
public class BatteryVehicle {
    @TableId
    private Integer vid;
    private String batteryType;
    private Integer totalMileage;
    private Integer batteryHealth;
}
