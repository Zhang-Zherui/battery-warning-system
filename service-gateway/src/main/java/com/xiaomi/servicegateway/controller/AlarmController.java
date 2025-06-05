package com.xiaomi.servicegateway.controller;

import com.xiaomi.domain.signal.api.SignalServiceApi;
import com.xiaomi.domain.signal.dto.WarningDTO;
import com.xiaomi.common.core.Result;
import com.xiaomi.domain.warning.api.WarningServiceApi;
import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import com.xiaomi.servicegateway.service.RocketMQListenerService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api")
public class AlarmController {
    @DubboReference
    private SignalServiceApi signalServiceApi;
    @DubboReference
    private WarningServiceApi warningServiceApi;
    @Autowired
    private RocketMQListenerService rocketMQListenerService;

    @PostMapping("/report")
    public Result<Object> reportWarnings(@RequestBody WarningDTO warning) {
        System.out.println("Received warning: " + warning);

        // 异步处理
        signalServiceApi.processSignal(warning);
        int carId = warning.getCarId();
        Integer warnId = warning.getWarnId();

        if (warnId != null) {
            AlertMessageDTO alert = rocketMQListenerService.getProcessedMessage(carId, warnId);
            if (alert != null) {
                return Result.success(alert);
            } else {
                return Result.fail("No alert message found for carId: " + carId + ", warnId: " + warnId);
            }
        } else {
            AlertMessageDTO alert1 = rocketMQListenerService.getProcessedMessage(carId, 1);
            AlertMessageDTO alert2 = rocketMQListenerService.getProcessedMessage(carId, 2);
            List<AlertMessageDTO> results = new ArrayList<>();
            if (alert1 != null) results.add(alert1);
            if (alert2 != null) results.add(alert2);
            if (!results.isEmpty()) {
                return Result.success(results);
            } else {
                return Result.fail("No alert messages found for carId: " + carId);
            }
        }
    }

    @GetMapping("/car")
    public Result<?> getAlertMessages(@RequestParam("carId") Integer carId, @RequestParam(value = "warnId", required = false) Integer warnId) {
        // 如果提供了 warnId 参数，返回单个警告信息
        if (warnId != null) {
            AlertMessageDTO alert = warningServiceApi.getAlertMessage(carId, warnId);
            if (alert != null) {
                return Result.success(alert);
            } else {
                return Result.fail("No alert message found for carId: " + carId + ", warnId: " + warnId);
            }
        }

        // 如果没有提供 warnId 参数，返回警告信息列表
        AlertMessageDTO alert1 = rocketMQListenerService.getProcessedMessage(carId, 1);
        AlertMessageDTO alert2 = rocketMQListenerService.getProcessedMessage(carId, 2);

        List<AlertMessageDTO> results = new ArrayList<>();
        if (alert1 != null) results.add(alert1);
        if (alert2 != null) results.add(alert2);

        if (!results.isEmpty()) {
            return Result.success(results);
        } else {
            return Result.fail("No alert messages found for carId: " + carId);
        }
    }

}
