package com.xiaomi.domain.warning.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertMessageDTO {
    private Integer carId;
    private Integer warnId;
    private Integer alertLevel;
    private String batteryType;
}

