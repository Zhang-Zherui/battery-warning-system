package com.xiaomi.servicewarning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomi.domain.warning.entity.AlertMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlertMessageMapper extends BaseMapper<AlertMessage> {
    @Insert("<script>" +
            "INSERT INTO alert_message (car_id, warn_id, alert_level, battery_type, created_at) VALUES " +
            "<foreach collection='batchList' item='item' separator=','>" +
            "(#{item.carId}, #{item.warnId}, #{item.alertLevel}, #{item.batteryType}, #{item.createdAt})" +
            "</foreach>" +
            "</script>")
    void insertBatchSomeColumn(@Param("batchList") List<AlertMessage> batchList);
}
