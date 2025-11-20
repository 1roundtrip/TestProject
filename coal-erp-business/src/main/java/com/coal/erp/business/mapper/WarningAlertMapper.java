package com.coal.erp.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.WarningAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预警记录Mapper
 */
@Mapper
public interface WarningAlertMapper extends BaseMapper<WarningAlert> {
    
    /**
     * 根据预警级别查询未处理的预警
     */
    List<WarningAlert> selectByLevelAndStatus(@Param("alertLevel") String alertLevel, @Param("status") String status);
}















