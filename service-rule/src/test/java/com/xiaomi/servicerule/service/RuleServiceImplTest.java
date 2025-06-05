package com.xiaomi.servicerule.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomi.domain.rule.entity.WarningRule;
import com.xiaomi.servicerule.mapper.WarningRuleMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RuleServiceImplTest {

    @Mock
    private WarningRuleMapper mockWarningRuleMapper;
    @Mock
    private StringRedisTemplate mockRedisTemplate;

    @InjectMocks
    private RuleServiceImpl ruleServiceImplUnderTest;

    @Test
    public void testGetAlertLevelByWarnIdAndBatteryType() throws Exception {
        // Setup
        when(mockRedisTemplate.opsForHash()).thenReturn(null);

        // Configure WarningRuleMapper.selectList(...).
        final WarningRule warningRule = new WarningRule();
        warningRule.setId(0);
        warningRule.setWarnType(0);
        warningRule.setRangeStart(0.0);
        warningRule.setRangeEnd(0.0);
        warningRule.setAlertLevel(0);
        final List<WarningRule> warningRules = Arrays.asList(warningRule);
        when(mockWarningRuleMapper.selectList(any(QueryWrapper.class))).thenReturn(warningRules);

        // Run the test
        final int result = ruleServiceImplUnderTest.getAlertLevelByWarnIdAndBatteryType(0, "batteryType", 0.0, 0.0);

        // Verify the results
        assertEquals(0, result);
    }

    @Test
    public void testGetAlertLevelByWarnIdAndBatteryType_WarningRuleMapperReturnsNoItems() throws Exception {
        // Setup
        when(mockRedisTemplate.opsForHash()).thenReturn(null);
        when(mockWarningRuleMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final int result = ruleServiceImplUnderTest.getAlertLevelByWarnIdAndBatteryType(0, "batteryType", 0.0, 0.0);

        // Verify the results
        assertEquals(0, result);
    }
}
