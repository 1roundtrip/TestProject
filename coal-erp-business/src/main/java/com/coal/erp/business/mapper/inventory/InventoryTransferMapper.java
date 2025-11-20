package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryTransfer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调拨单Mapper
 */
@Mapper
public interface InventoryTransferMapper extends BaseMapper<InventoryTransfer> {
}

