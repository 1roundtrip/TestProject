package com.coal.erp.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.purchase.PurchaseOrderDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购订单服务接口
 */
public interface IPurchaseOrderService extends IService<PurchaseOrder> {
    
    /**
     * 创建采购订单
     */
    R<?> createOrder(PurchaseOrder order, List<PurchaseOrderDetail> details);
    
    /**
     * 提交审批
     */
    R<?> submitOrder(Long orderId);
    
    /**
     * 审批通过
     */
    R<?> approveOrder(Long orderId, String approveRemark);
    
    /**
     * 确认订单
     */
    R<?> confirmOrder(Long orderId);
    
    /**
     * 分页查询
     */
    Page<PurchaseOrder> pageOrder(Long current, Long size, String orderNo, String status, Long supplierId);
    
    /**
     * 获取订单明细
     */
    List<PurchaseOrderDetail> getOrderDetails(Long orderId);
    
    /**
     * 从采购申请创建订单
     */
    R<?> createOrderFromRequisition(Long requisitionId, Long supplierId);
}











