package com.xiaomi.servicewarning.service;

import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import com.xiaomi.domain.warning.entity.AlertMessage;
import com.xiaomi.servicewarning.mapper.AlertMessageMapper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.MessagingException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WarningServiceImplTest {

    @Mock
    private RocketMQTemplate mockRocketMQTemplate;
    @Mock
    private AlertMessageMapper mockAlertMessageMapper;

    @InjectMocks
    private WarningServiceImpl warningServiceImplUnderTest;

    @Test
    public void testUpdateWarning() {
        // Setup
        // Run the test
        warningServiceImplUnderTest.updateWarning();

        // Verify the results
        // Confirm AlertMessageMapper.insertBatchSomeColumn(...).
        final AlertMessage alertMessage = new AlertMessage();
        alertMessage.setCarId(0);
        alertMessage.setWarnId(0);
        alertMessage.setAlertLevel(0);
        alertMessage.setBatteryType("batteryType");
        alertMessage.setCreatedAt(Timestamp.valueOf(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0)));
        final List<AlertMessage> batchList = Arrays.asList(alertMessage);
        verify(mockAlertMessageMapper).insertBatchSomeColumn(batchList);
        verify(mockRocketMQTemplate).convertAndSend("WarningTopic", new AlertMessageDTO(0, 0, 0, "batteryType"));
    }

    @Test(expected = MessagingException.class)
    public void testUpdateWarning_RocketMQTemplateThrowsMessagingException() {
        // Setup
        doThrow(MessagingException.class).when(mockRocketMQTemplate).convertAndSend("WarningTopic",
                new AlertMessageDTO(0, 0, 0, "batteryType"));

        // Run the test
        warningServiceImplUnderTest.updateWarning();
    }

    @Test
    public void testGetAlertMessage() throws Exception {
        // Setup
        final AlertMessageDTO expectedResult = new AlertMessageDTO(0, 0, 0, "batteryType");

        // Run the test
        final AlertMessageDTO result = warningServiceImplUnderTest.getAlertMessage(0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
