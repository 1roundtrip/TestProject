package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryLocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库位Mapper
 */
@Mapper
public interface InventoryLocationMapper extends BaseMapper<InventoryLocation> {
}

