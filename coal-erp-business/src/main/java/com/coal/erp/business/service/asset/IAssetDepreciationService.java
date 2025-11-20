package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.asset.AssetDepreciation;
import com.coal.erp.business.domain.asset.AssetDepreciationDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 资产折旧服务接口
 */
public interface IAssetDepreciationService extends IService<AssetDepreciation> {
    
    /**
     * 配置资产折旧
     */
    R<?> configDepreciation(AssetDepreciation depreciation);
    
    /**
     * 计提折旧（按月）
     */
    R<?> calculateDepreciation(String month);
    
    /**
     * 确认折旧
     */
    R<?> confirmDepreciation(Long detailId);
    
    /**
     * 分页查询
     */
    Page<AssetDepreciation> pageDepreciation(Long current, Long size, String assetCode, String status);
    
    /**
     * 获取折旧明细
     */
    List<AssetDepreciationDetail> getDepreciationDetails(Long depreciationId);
    
    /**
     * 获取月度折旧明细
     */
    List<AssetDepreciationDetail> getMonthDepreciationDetails(String month);
}

