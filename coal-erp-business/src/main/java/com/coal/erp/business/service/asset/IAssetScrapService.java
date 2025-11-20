package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.asset.AssetScrap;
import com.coal.erp.common.core.domain.R;

/**
 * 资产报废服务接口
 */
public interface IAssetScrapService extends IService<AssetScrap> {
    
    /**
     * 创建报废申请
     */
    R<?> createScrap(AssetScrap scrap);
    
    /**
     * 审批报废申请
     */
    R<?> approveScrap(Long scrapId, String approveRemark);
    
    /**
     * 驳回报废申请
     */
    R<?> rejectScrap(Long scrapId, String rejectRemark);
    
    /**
     * 完成报废
     */
    R<?> completeScrap(Long scrapId);
    
    /**
     * 分页查询
     */
    Page<AssetScrap> pageScrap(Long current, Long size, String scrapNo, String status);
}

