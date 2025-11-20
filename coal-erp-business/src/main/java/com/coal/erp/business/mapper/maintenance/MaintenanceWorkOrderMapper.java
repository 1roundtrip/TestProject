package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修工单Mapper
 */
@Mapper
public interface MaintenanceWorkOrderMapper extends BaseMapper<MaintenanceWorkOrder> {
}

