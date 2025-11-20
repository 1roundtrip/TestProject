package com.coal.erp.business.mapper.asset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.asset.AssetBorrow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资产领用退库Mapper
 */
@Mapper
public interface AssetBorrowMapper extends BaseMapper<AssetBorrow> {
}

