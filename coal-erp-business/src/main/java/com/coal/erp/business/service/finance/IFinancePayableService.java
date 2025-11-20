package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.FinancePayable;
import com.coal.erp.common.core.domain.R;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 应付单据服务接口
 */
public interface IFinancePayableService extends IService<FinancePayable> {
    
    /**
     * 创建应付单据
     */
    R<?> createPayable(FinancePayable payable);
    
    /**
     * 更新应付单据
     */
    R<?> updatePayable(FinancePayable payable);
    
    /**
     * 核销应付单据
     */
    R<?> settlePayable(Long payableId, BigDecimal settleAmount);
    
    /**
     * 作废应付单据
     */
    R<?> cancelPayable(Long payableId, String reason);
    
    /**
     * 获取供应商应付余额
     */
    R<?> getSupplierPayableBalance(Long supplierId);
    
    /**
     * 获取应付账龄分析
     */
    R<?> getPayableAgingAnalysis(Date asOfDate);
    
    /**
     * 创建付款计划
     */
    R<?> createPaymentPlan(Long payableId, Date planDate, BigDecimal amount);
    
    /**
     * 处理预付款
     */
    R<?> handleAdvancePayment(Long supplierId, BigDecimal amount);
}