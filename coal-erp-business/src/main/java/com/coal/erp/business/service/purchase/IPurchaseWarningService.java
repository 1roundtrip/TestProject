package com.coal.erp.business.service.purchase;

import com.coal.erp.common.core.domain.R;

/**
 * 采购预警服务接口
 */
public interface IPurchaseWarningService {
    
    /**
     * 检查订单超期预警
     */
    R<?> checkOrderOverdue();
    
    /**
     * 检查付款超期预警
     */
    R<?> checkPaymentOverdue();
    
    /**
     * 检查质量问题预警
     */
    R<?> checkQualityIssue();
    
    /**
     * 检查供应商风险预警
     */
    R<?> checkSupplierRisk();
}

