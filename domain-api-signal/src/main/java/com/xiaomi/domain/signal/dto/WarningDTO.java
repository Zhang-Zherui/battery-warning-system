package com.xiaomi.domain.signal.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class WarningDTO implements Serializable {
    private Integer carId;
    private Integer warnId;
    private String signal;
}
