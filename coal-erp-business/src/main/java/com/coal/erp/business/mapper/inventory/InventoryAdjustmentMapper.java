package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryAdjustment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存调整单Mapper
 */
@Mapper
public interface InventoryAdjustmentMapper extends BaseMapper<InventoryAdjustment> {
}

