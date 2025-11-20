package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryMaterial;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存物品Mapper
 */
@Mapper
public interface InventoryMaterialMapper extends BaseMapper<InventoryMaterial> {
}

