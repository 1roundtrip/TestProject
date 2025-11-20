package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenancePlan;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预防性维护计划Mapper
 */
@Mapper
public interface MaintenancePlanMapper extends BaseMapper<MaintenancePlan> {
}

