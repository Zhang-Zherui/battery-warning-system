package com.xiaomi.servicesignal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomi.domain.signal.entity.BatterySignal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BatterySignalMapper extends BaseMapper<BatterySignal> {
}
