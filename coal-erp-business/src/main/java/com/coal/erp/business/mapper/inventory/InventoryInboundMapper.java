package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryInbound;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单Mapper
 */
@Mapper
public interface InventoryInboundMapper extends BaseMapper<InventoryInbound> {
}

