package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryStocktaking;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存盘点Mapper
 */
@Mapper
public interface InventoryStocktakingMapper extends BaseMapper<InventoryStocktaking> {
}

