package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.FinancePayment;
import com.coal.erp.common.core.domain.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收付款服务接口
 */
public interface IFinancePaymentService extends IService<FinancePayment> {
    
    /**
     * 创建收款单
     */
    R<?> createReceivePayment(FinancePayment payment, List<Long> receivableIds);
    
    /**
     * 创建付款单
     */
    R<?> createPayPayment(FinancePayment payment, List<Long> payableIds);
    
    /**
     * 创建预收预付单
     */
    R<?> createAdvancePayment(FinancePayment payment);
    
    /**
     * 确认收付款单
     */
    R<?> confirmPayment(Long paymentId);
    
    /**
     * 取消收付款单
     */
    R<?> cancelPayment(Long paymentId, String reason);
    
    /**
     * 核销预收预付
     */
    R<?> settleAdvancePayment(Long advancePaymentId, List<Long> sourceIds);
    
    /**
     * 获取客户收款记录
     */
    R<?> getCustomerPaymentHistory(Long customerId);
    
    /**
     * 获取供应商付款记录
     */
    R<?> getSupplierPaymentHistory(Long supplierId);
    
    /**
     * 处理多币种结算
     */
    R<?> handleMultiCurrencySettlement(Long paymentId, String targetCurrency, BigDecimal exchangeRate);
}