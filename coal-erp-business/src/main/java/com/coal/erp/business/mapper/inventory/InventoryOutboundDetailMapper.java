package com.coal.erp.business.mapper.inventory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.inventory.InventoryOutboundDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库明细Mapper
 */
@Mapper
public interface InventoryOutboundDetailMapper extends BaseMapper<InventoryOutboundDetail> {
}

