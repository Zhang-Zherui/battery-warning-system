package com.xiaomi.domain.signal.api;

import com.xiaomi.domain.signal.dto.WarningDTO;
import com.xiaomi.domain.signal.entity.BatterySignal;

import java.util.List;

public interface SignalServiceApi {
    void processSignal(WarningDTO warningDTO);
    List<BatterySignal> getAllBatterySignal();
    BatterySignal getBatterySignalByCarIdAndWarnId(int carId, int warnId);
}
