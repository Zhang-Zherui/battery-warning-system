package com.xiaomi.servicesignal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaomi.domain.signal.api.SignalServiceApi;
import com.alibaba.fastjson.JSON;
import com.xiaomi.domain.signal.dto.WarningDTO;
import com.xiaomi.domain.signal.entity.BatterySignal;
import com.xiaomi.servicesignal.mapper.BatterySignalMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@DubboService
@Service
public class SignalServiceImpl implements SignalServiceApi {

    @Autowired
    private BatterySignalMapper batterySignalMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void processSignal(WarningDTO signalReportDTO) {
        if (signalReportDTO == null || signalReportDTO.getSignal() == null || signalReportDTO.getSignal().isEmpty()) {
            throw new IllegalArgumentException("Invalid signal data.");
        }

        Integer carId = signalReportDTO.getCarId();
        String signalJson = signalReportDTO.getSignal();
        Integer warnId = signalReportDTO.getWarnId();

        if (warnId != null) {
            BatterySignal batterySignal = buildSignal(carId, warnId, signalJson);
            saveToRedisAndDB(batterySignal);
        } else {
            BatterySignal signal1 = buildSignal(carId, 1, signalJson);
            BatterySignal signal2 = buildSignal(carId, 2, signalJson);
            saveToRedisAndDB(signal1);
            saveToRedisAndDB(signal2);
        }
    }

    private BatterySignal buildSignal(Integer carId, Integer warnId, String signalJson) {
        BatterySignal batterySignal = new BatterySignal();
        batterySignal.setCarId(carId);
        batterySignal.setWarnId(warnId);

        if (warnId == 1) {
            Double max = getValueFromJson(signalJson, "Mx");
            Double min = getValueFromJson(signalJson, "Mi");
            batterySignal.setMax(max);
            batterySignal.setMin(min);
        } else if (warnId == 2) {
            Double max = getValueFromJson(signalJson, "Ix");
            Double min = getValueFromJson(signalJson, "Ii");
            batterySignal.setMax(max);
            batterySignal.setMin(min);
        }
        return batterySignal;
    }


    private void saveToRedisAndDB(BatterySignal batterySignal) {
        String redisKey = "batterySignal:" + batterySignal.getCarId() + ":" + batterySignal.getWarnId();
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(batterySignal), 1, TimeUnit.SECONDS);
        batterySignalMapper.insert(batterySignal);
    }

    private Double getValueFromJson(String signalJson, String key) {
        return JSON.parseObject(signalJson).getDouble(key);
    }
    @Override
    public List<BatterySignal> getAllBatterySignal() {
        Set<String> redisKeys = redisTemplate.keys("batterySignal:*");
        if (redisKeys.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> jsonList = redisTemplate.opsForValue().multiGet(redisKeys);
        List<BatterySignal> batterySignals = new ArrayList<>();

        if (jsonList != null) {
            for (String json : jsonList) {
                if (json != null) {
                    BatterySignal signal = JSON.parseObject(json, BatterySignal.class);
                    batterySignals.add(signal);
                }
            }
        }

        return batterySignals;
    }

    public BatterySignal getRealtimeSignal(int carId, int warnId) {
        String key = "batterySignal:" + carId + ":" + warnId;
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            return JSON.parseObject(json, BatterySignal.class);
        }
        return null;
    }
    public BatterySignal getLatestSignal(int carId, int warnId) {
        QueryWrapper<BatterySignal> wrapper = Wrappers.query();
        wrapper.eq("car_id", carId)
                .eq("warn_id", warnId)
                .orderByDesc("id") // 假设 id 是自增主键，越大越新
                .last("LIMIT 1");

        return batterySignalMapper.selectOne(wrapper);
    }
    @Override
    public BatterySignal getBatterySignalByCarIdAndWarnId(int carId, int warnId) {
        BatterySignal realtimeSignal = getRealtimeSignal(carId, warnId);
        if (realtimeSignal != null) {
            return realtimeSignal;
        }
        return getLatestSignal(carId, warnId);
    }


}
