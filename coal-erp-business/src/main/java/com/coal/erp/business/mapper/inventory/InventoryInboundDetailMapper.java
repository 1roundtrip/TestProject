package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryInboundDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库明细Mapper
 */
@Mapper
public interface InventoryInboundDetailMapper extends BaseMapper<InventoryInboundDetail> {
}

