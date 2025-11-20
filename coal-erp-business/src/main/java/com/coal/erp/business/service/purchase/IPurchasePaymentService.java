package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchasePayment;
import com.coal.erp.business.domain.purchase.PurchasePaymentDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购付款服务接口
 */
public interface IPurchasePaymentService extends IService<PurchasePayment> {
    
    /**
     * 创建付款单
     */
    R<?> createPayment(PurchasePayment payment, List<PurchasePaymentDetail> details);
    
    /**
     * 提交审批
     */
    R<?> submitPayment(Long paymentId);
    
    /**
     * 审批通过
     */
    R<?> approvePayment(Long paymentId, String approveRemark);
    
    /**
     * 确认付款
     */
    R<?> confirmPayment(Long paymentId);
    
    /**
     * 分页查询
     */
    Page<PurchasePayment> pagePayment(Long current, Long size, String paymentNo, String status);
    
    /**
     * 获取付款明细
     */
    List<PurchasePaymentDetail> getPaymentDetails(Long paymentId);
    
    /**
     * 从采购订单创建付款单
     */
    R<?> createPaymentFromOrder(Long orderId, String paymentType);
}

