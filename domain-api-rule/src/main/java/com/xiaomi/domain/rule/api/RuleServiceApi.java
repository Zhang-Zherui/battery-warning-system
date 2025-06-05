package com.xiaomi.domain.rule.api;

public interface RuleServiceApi {

    /**
     * 根据规则编号 (warnId) 和电池类型 (batteryType) 获取预警等级
     * @param warnId 规则编号
     * @param batteryType 电池类型（三元电池 / 铁锂电池）
     * @param value1 Mx 或 Ix
     * @param value2 Mi 或 Ii
     * @return 预警等级
     */
    int getAlertLevelByWarnIdAndBatteryType(int warnId, String batteryType, double value1, double value2);
}


