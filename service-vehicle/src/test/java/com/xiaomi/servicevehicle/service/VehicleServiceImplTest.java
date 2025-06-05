package com.xiaomi.servicevehicle.service;

import com.xiaomi.domain.vehicle.entity.BatteryVehicle;
import com.xiaomi.servicevehicle.mapper.BatteryVehicleMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceImplTest {

    @Mock
    private BatteryVehicleMapper mockBatteryVehicleMapper;
    @Mock
    private StringRedisTemplate mockRedisTemplate;

    @InjectMocks
    private VehicleServiceImpl vehicleServiceImplUnderTest;

    @Test
    public void testGetBatteryTypeByVid() throws Exception {
        // Setup
        when(mockRedisTemplate.opsForValue()).thenReturn(null);

        // Configure BatteryVehicleMapper.selectById(...).
        final BatteryVehicle batteryVehicle = new BatteryVehicle();
        batteryVehicle.setVid(0);
        batteryVehicle.setBatteryType("Vehicle not found");
        batteryVehicle.setTotalMileage(0);
        batteryVehicle.setBatteryHealth(0);
        when(mockBatteryVehicleMapper.selectById(0)).thenReturn(batteryVehicle);

        // Run the test
        final String result = vehicleServiceImplUnderTest.getBatteryTypeByVid(0);

        // Verify the results
        assertEquals("Vehicle not found", result);
    }

    @Test
    public void testGetBatteryTypeByVid_BatteryVehicleMapperReturnsNull() throws Exception {
        // Setup
        when(mockRedisTemplate.opsForValue()).thenReturn(null);
        when(mockBatteryVehicleMapper.selectById(0)).thenReturn(null);

        // Run the test
        final String result = vehicleServiceImplUnderTest.getBatteryTypeByVid(0);

        // Verify the results
        assertEquals("Vehicle not found", result);
    }
}
