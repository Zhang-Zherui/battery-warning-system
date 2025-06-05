package com.xiaomi.servicerule.service;

import com.xiaomi.domain.rule.api.RuleServiceApi;
import com.xiaomi.domain.rule.entity.WarningRule;
import com.xiaomi.servicerule.mapper.WarningRuleMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@DubboService
@Service
public class RuleServiceImpl implements RuleServiceApi {

    @Autowired
    private WarningRuleMapper warningRuleMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String RULE_CACHE_PREFIX = "rule:";

    @Override
    public int getAlertLevelByWarnIdAndBatteryType(int warnType, String batteryType, double value1, double value2) {
        double difference = value1 - value2;
        String redisKey = RULE_CACHE_PREFIX + warnType + ":" + batteryType;
        Map<Object, Object> cachedRules = redisTemplate.opsForHash().entries(redisKey);
        if (!cachedRules.isEmpty()) {
            for (Map.Entry<Object, Object> entry : cachedRules.entrySet()) {
                String[] rangeAndLevel = ((String) entry.getValue()).split(",");
                double rangeStart = Double.parseDouble(rangeAndLevel[0]);
                double rangeEnd = Double.parseDouble(rangeAndLevel[1]);
                int alertLevel = Integer.parseInt(rangeAndLevel[2]);
                if (difference >= rangeStart && difference < rangeEnd) {
                    return alertLevel;
                }
            }
        }
        List<WarningRule> rules = warningRuleMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<WarningRule>()
                        .eq("warn_type", warnType)
                        .eq("battery_type", batteryType)
                        .eq("status", 1)
                        .orderByAsc("range_start")
        );
        if (rules.isEmpty()) {
            return -1;
        }
        Map<Object, Object> ruleMap = rules.stream()
                .collect(Collectors.toMap(
                        WarningRule::getRangeStart,
                        rule -> rule.getRangeStart() + "," + rule.getRangeEnd() + "," + rule.getAlertLevel()
                ));
        redisTemplate.opsForHash().putAll(redisKey, ruleMap);
        for (WarningRule rule : rules) {
            if (difference >= rule.getRangeStart() && difference < rule.getRangeEnd()) {
                return rule.getAlertLevel();
            }
        }
        return -1;
    }
}

