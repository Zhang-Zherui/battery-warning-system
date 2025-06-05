package com.xiaomi.servicewarning.service;

import com.xiaomi.domain.rule.api.RuleServiceApi;
import com.xiaomi.domain.signal.api.SignalServiceApi;
import com.xiaomi.domain.vehicle.api.VehicleServiceApi;
import com.xiaomi.domain.warning.api.WarningServiceApi;
import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import com.xiaomi.domain.warning.entity.AlertMessage;
import com.xiaomi.domain.signal.entity.BatterySignal;
import com.xiaomi.servicewarning.mapper.AlertMessageMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@DubboService
public class WarningServiceImpl implements WarningServiceApi {

    @DubboReference
    private SignalServiceApi signalService;

    @DubboReference
    private VehicleServiceApi vehicleServiceApi;

    @DubboReference
    private RuleServiceApi ruleServiceApi;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private AlertMessageMapper alertMessageMapper;

    private static final int BATCH_SIZE = 100;
    private final LinkedBlockingQueue<AlertMessage> alertMessageQueue = new LinkedBlockingQueue<>();


    @Scheduled(fixedRate = 500)  // 每500毫秒执行一次
    public void updateWarning() {
        List<BatterySignal> batterySignals = signalService.getAllBatterySignal();
        for (BatterySignal batterySignal : batterySignals) {
            Integer carId = batterySignal.getCarId();
            Integer warnId = batterySignal.getWarnId();
            String batteryType = vehicleServiceApi.getBatteryTypeByVid(carId);
            double value1 = batterySignal.getMax();
            double value2 = batterySignal.getMin();
            int alertLevel = ruleServiceApi.getAlertLevelByWarnIdAndBatteryType(warnId, batteryType, value1, value2);
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.setCarId(carId);
            alertMessage.setWarnId(warnId);
            alertMessage.setAlertLevel(alertLevel);
            alertMessage.setBatteryType(batteryType);
            alertMessageQueue.offer(alertMessage);
            if (alertMessageQueue.size() >= BATCH_SIZE) {
                insertBatch();
            }
            rocketMQTemplate.convertAndSend("WarningTopic", new AlertMessageDTO(carId, warnId, alertLevel, batteryType));  // 发送消息到 RocketMQ
        }
    }

    public AlertMessageDTO getAlertMessage(Integer carId, Integer warnId) {
        String batteryType = vehicleServiceApi.getBatteryTypeByVid(carId);
        BatterySignal batterySignal = signalService.getBatterySignalByCarIdAndWarnId(carId, warnId);
        double value1 = batterySignal.getMax();
        double value2 = batterySignal.getMin();
        int alertLevel = ruleServiceApi.getAlertLevelByWarnIdAndBatteryType(warnId, batteryType, value1, value2);
        return new AlertMessageDTO(carId, warnId, alertLevel, batteryType);
    }

    private void insertBatch() {
        if (!alertMessageQueue.isEmpty()) {
            List<AlertMessage> batchList = new ArrayList<>();
            alertMessageQueue.drainTo(batchList, BATCH_SIZE);
            alertMessageMapper.insertBatchSomeColumn(batchList);
        }
    }

}
