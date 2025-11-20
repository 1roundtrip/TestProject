package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存明细Mapper
 */
@Mapper
public interface InventoryStockMapper extends BaseMapper<InventoryStock> {
}

