package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenanceCost;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修成本Mapper
 */
@Mapper
public interface MaintenanceCostMapper extends BaseMapper<MaintenanceCost> {
}

