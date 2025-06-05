package com.xiaomi.servicevehicle.service;

import com.xiaomi.domain.vehicle.api.VehicleServiceApi;
import com.xiaomi.domain.vehicle.entity.BatteryVehicle;
import com.xiaomi.servicevehicle.mapper.BatteryVehicleMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class VehicleServiceImpl implements VehicleServiceApi {

    @Autowired
    private BatteryVehicleMapper batteryVehicleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;  // Redis 操作模板

    @Override
    public String getBatteryTypeByVid(Integer vid) {
        String redisKey = "vehicle:" + vid + ":batteryType";
        String batteryType = redisTemplate.opsForValue().get(redisKey);
        if (batteryType != null) {
            return batteryType;
        }
        BatteryVehicle vehicle = batteryVehicleMapper.selectById(vid);
        if (vehicle != null) {
            batteryType = vehicle.getBatteryType();
            redisTemplate.opsForValue().set(redisKey, batteryType, 1, java.util.concurrent.TimeUnit.MINUTES);
            return batteryType;
        } else {
            return "Vehicle not found";
        }
    }
}
