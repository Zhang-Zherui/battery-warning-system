package com.xiaomi.servicesignal.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomi.domain.signal.dto.WarningDTO;
import com.xiaomi.domain.signal.entity.BatterySignal;
import com.xiaomi.servicesignal.mapper.BatterySignalMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SignalServiceImplTest {

    @Mock
    private BatterySignalMapper mockBatterySignalMapper;
    @Mock
    private StringRedisTemplate mockRedisTemplate;

    @InjectMocks
    private SignalServiceImpl signalServiceImplUnderTest;

    @Test
    public void testProcessSignal() {
        // Setup
        final WarningDTO signalReportDTO = new WarningDTO();
        signalReportDTO.setCarId(0);
        signalReportDTO.setWarnId(0);
        signalReportDTO.setSignal("signal");

        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        signalServiceImplUnderTest.processSignal(signalReportDTO);

        // Verify the results
        // Confirm BatterySignalMapper.insert(...).
        final BatterySignal entity = new BatterySignal();
        entity.setCarId(0);
        entity.setWarnId(0);
        entity.setMax(0.0);
        entity.setMin(0.0);
        verify(mockBatterySignalMapper).insert(entity);
    }

    @Test
    public void testGetAllBatterySignal() throws Exception {
        // Setup
        final BatterySignal batterySignal = new BatterySignal();
        batterySignal.setCarId(0);
        batterySignal.setWarnId(0);
        batterySignal.setMax(0.0);
        batterySignal.setMin(0.0);
        final List<BatterySignal> expectedResult = Arrays.asList(batterySignal);
        when(mockRedisTemplate.keys("batterySignal:*")).thenReturn(new HashSet<>(Arrays.asList("value")));
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        final List<BatterySignal> result = signalServiceImplUnderTest.getAllBatterySignal();

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAllBatterySignal_StringRedisTemplateKeysReturnsNull() throws Exception {
        // Setup
        final BatterySignal batterySignal = new BatterySignal();
        batterySignal.setCarId(0);
        batterySignal.setWarnId(0);
        batterySignal.setMax(0.0);
        batterySignal.setMin(0.0);
        final List<BatterySignal> expectedResult = Arrays.asList(batterySignal);
        when(mockRedisTemplate.keys("batterySignal:*")).thenReturn(null);
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        final List<BatterySignal> result = signalServiceImplUnderTest.getAllBatterySignal();

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetAllBatterySignal_StringRedisTemplateKeysReturnsNoItems() throws Exception {
        // Setup
        when(mockRedisTemplate.keys("batterySignal:*")).thenReturn(Collections.emptySet());

        // Run the test
        final List<BatterySignal> result = signalServiceImplUnderTest.getAllBatterySignal();

        // Verify the results
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetRealtimeSignal() throws Exception {
        // Setup
        final BatterySignal expectedResult = new BatterySignal();
        expectedResult.setCarId(0);
        expectedResult.setWarnId(0);
        expectedResult.setMax(0.0);
        expectedResult.setMin(0.0);

        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Run the test
        final BatterySignal result = signalServiceImplUnderTest.getRealtimeSignal(0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetLatestSignal() throws Exception {
        // Setup
        final BatterySignal expectedResult = new BatterySignal();
        expectedResult.setCarId(0);
        expectedResult.setWarnId(0);
        expectedResult.setMax(0.0);
        expectedResult.setMin(0.0);

        // Configure BatterySignalMapper.selectOne(...).
        final BatterySignal batterySignal = new BatterySignal();
        batterySignal.setCarId(0);
        batterySignal.setWarnId(0);
        batterySignal.setMax(0.0);
        batterySignal.setMin(0.0);
        when(mockBatterySignalMapper.selectOne(any(QueryWrapper.class))).thenReturn(batterySignal);

        // Run the test
        final BatterySignal result = signalServiceImplUnderTest.getLatestSignal(0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetBatterySignalByCarIdAndWarnId() throws Exception {
        // Setup
        final BatterySignal expectedResult = new BatterySignal();
        expectedResult.setCarId(0);
        expectedResult.setWarnId(0);
        expectedResult.setMax(0.0);
        expectedResult.setMin(0.0);

        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Configure BatterySignalMapper.selectOne(...).
        final BatterySignal batterySignal = new BatterySignal();
        batterySignal.setCarId(0);
        batterySignal.setWarnId(0);
        batterySignal.setMax(0.0);
        batterySignal.setMin(0.0);
        when(mockBatterySignalMapper.selectOne(any(QueryWrapper.class))).thenReturn(batterySignal);

        // Run the test
        final BatterySignal result = signalServiceImplUnderTest.getBatterySignalByCarIdAndWarnId(0, 0);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
