package com.coal.erp.business.mapper.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.asset.AssetInventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资产盘点Mapper
 */
@Mapper
public interface AssetInventoryMapper extends BaseMapper<AssetInventory> {
}

