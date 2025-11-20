package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchaseReceiving;
import com.coal.erp.business.domain.purchase.PurchaseReceivingDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购收货服务接口
 */
public interface IPurchaseReceivingService extends IService<PurchaseReceiving> {
    
    /**
     * 创建收货单
     */
    R<?> createReceiving(PurchaseReceiving receiving, List<PurchaseReceivingDetail> details);
    
    /**
     * 确认收货
     */
    R<?> confirmReceiving(Long receivingId);
    
    /**
     * 分页查询
     */
    Page<PurchaseReceiving> pageReceiving(Long current, Long size, String receivingNo, String status);
    
    /**
     * 获取收货明细
     */
    List<PurchaseReceivingDetail> getReceivingDetails(Long receivingId);
    
    /**
     * 从采购订单创建收货单
     */
    R<?> createReceivingFromOrder(Long orderId);
}

