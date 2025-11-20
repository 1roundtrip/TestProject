package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheck;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheckDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购质检服务接口
 */
public interface IPurchaseQualityCheckService extends IService<PurchaseQualityCheck> {
    
    /**
     * 创建质检单
     */
    R<?> createQualityCheck(PurchaseQualityCheck qualityCheck, List<PurchaseQualityCheckDetail> details);
    
    /**
     * 完成质检
     */
    R<?> completeQualityCheck(Long checkId);
    
    /**
     * 分页查询
     */
    Page<PurchaseQualityCheck> pageQualityCheck(Long current, Long size, String checkNo, String status);
    
    /**
     * 获取质检明细
     */
    List<PurchaseQualityCheckDetail> getQualityCheckDetails(Long checkId);
    
    /**
     * 从收货单创建质检单
     */
    R<?> createQualityCheckFromReceiving(Long receivingId);
}

