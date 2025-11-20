package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryStatistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存统计Mapper
 */
@Mapper
public interface InventoryStatisticsMapper extends BaseMapper<InventoryStatistics> {
}

