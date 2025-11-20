package com.coal.erp.business.service.finance;

import com.coal.erp.common.core.domain.R;

/**
 * 期末处理服务接口
 */
public interface IPeriodEndService {
    /**
     * 自动转账
     */
    R<?> autoTransfer(String period);
    
    /**
     * 期末调汇
     */
    R<?> exchangeAdjustment(String period);
    
    /**
     * 结转损益
     */
    R<?> transferProfitLoss(String period);
    
    /**
     * 期末结账
     */
    R<?> periodClosing(String period);
    
    /**
     * 反结账
     */
    R<?> reverseClosing(String period);
}