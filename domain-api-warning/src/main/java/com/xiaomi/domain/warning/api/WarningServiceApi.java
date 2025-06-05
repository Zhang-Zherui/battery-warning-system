package com.xiaomi.domain.warning.api;

import com.xiaomi.domain.warning.dto.AlertMessageDTO;

public interface WarningServiceApi {
    AlertMessageDTO getAlertMessage(Integer carId, Integer warnId);
}
