package com.xiaomi.servicegateway.service;

import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RocketMQListenerServiceTest {

    @Mock
    private RedisTemplate<String, AlertMessageDTO> mockRedisTemplate;

    @InjectMocks
    private RocketMQListenerService rocketMQListenerServiceUnderTest;

    @Test
    public void testOnMessage() {
        // Setup
        final AlertMessageDTO alertMessageDTO = new AlertMessageDTO(0, 0, 0, "batteryType");
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        rocketMQListenerServiceUnderTest.onMessage(alertMessageDTO);

        // Verify the results
    }

    @Test
    public void testGetProcessedMessage() throws Exception {
        // Setup
        final AlertMessageDTO expectedResult = new AlertMessageDTO(0, 0, 0, "batteryType");
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        final AlertMessageDTO result = rocketMQListenerServiceUnderTest.getProcessedMessage(0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
