package com.xiaomi.servicegateway.service;

import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RocketMQMessageListener(
        topic = "WarningTopic",
        consumerGroup = "warning-consumer-group"
)
public class RocketMQListenerService implements RocketMQListener<AlertMessageDTO> {

    @Autowired
    private RedisTemplate<String, AlertMessageDTO> redisTemplate;

    private static final String CACHE_PREFIX = "alert:";
    private static final long EXPIRY_TIME_SECONDS = 1;

    @Override
    public void onMessage(AlertMessageDTO alertMessageDTO) {
        int carId = alertMessageDTO.getCarId();
        int warnId = alertMessageDTO.getWarnId();
        String key = CACHE_PREFIX + carId + ":" + warnId;
        redisTemplate.opsForValue().set(key, alertMessageDTO, Duration.ofSeconds(EXPIRY_TIME_SECONDS));
    }

    public AlertMessageDTO getProcessedMessage(int carId, int warnId) {
        String key = CACHE_PREFIX + carId + ":" + warnId;
        return redisTemplate.opsForValue().get(key);
    }

}
