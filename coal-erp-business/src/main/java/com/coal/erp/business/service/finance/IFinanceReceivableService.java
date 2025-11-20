package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.FinanceReceivable;
import com.coal.erp.common.core.domain.R;
import java.math.BigDecimal;

import java.util.Date;

/**
 * 应收单据服务接口
 */
public interface IFinanceReceivableService extends IService<FinanceReceivable> {
    
    /**
     * 创建应收单据
     */
    R<?> createReceivable(FinanceReceivable receivable);
    
    /**
     * 更新应收单据
     */
    R<?> updateReceivable(FinanceReceivable receivable);
    
    /**
     * 核销应收单据
     */
    R<?> settleReceivable(Long receivableId, BigDecimal settleAmount);
    
    /**
     * 作废应收单据
     */
    R<?> cancelReceivable(Long receivableId, String reason);
    
    /**
     * 获取客户应收余额
     */
    R<?> getCustomerReceivableBalance(Long customerId);
    
    /**
     * 获取账龄分析报告
     */
    R<?> getAgingAnalysisReport(Date asOfDate);
    
    /**
     * 计提坏账准备
     */
    R<?> provisionBadDebt(Long receivableId, BigDecimal amount);
    
    /**
     * 核销坏账
     */
    R<?> writeOffBadDebt(Long receivableId);
    
    /**
     * 收回已核销坏账
     */
    R<?> recoverBadDebt(Long receivableId);
}