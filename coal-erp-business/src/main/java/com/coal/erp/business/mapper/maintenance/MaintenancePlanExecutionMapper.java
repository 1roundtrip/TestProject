package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenancePlanExecution;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维护计划执行记录Mapper
 */
@Mapper
public interface MaintenancePlanExecutionMapper extends BaseMapper<MaintenancePlanExecution> {
}

