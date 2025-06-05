package com.xiaomi.domain.vehicle.api;

public interface VehicleServiceApi {

    /**
     * 根据车架编号 (vid) 获取电池类型 (batteryType)
     * @param vid 车架编号
     * @return 电池类型
     */
    String getBatteryTypeByVid(Integer vid);
}

