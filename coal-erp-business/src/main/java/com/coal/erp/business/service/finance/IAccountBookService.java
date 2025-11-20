package com.coal.erp.business.service.finance;

import com.coal.erp.business.domain.finance.AccountBalance;
import com.coal.erp.business.domain.finance.VoucherDetail;
import com.coal.erp.common.core.domain.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 账簿查询服务接口
 */
public interface IAccountBookService {
    /**
     * 查询总账
     */
    R<List<AccountBalance>> getGeneralLedger(String period);
    
    /**
     * 查询明细账
     */
    R<List<VoucherDetail>> getDetailLedger(Long subjectId, String period);
    
    /**
     * 查询多栏账
     */
    R<Map<String, Object>> getMultiColumnLedger(Long subjectId, String period);
    
    /**
     * 查询余额表
     */
    R<List<AccountBalance>> getBalanceSheet(String period);
    
    /**
     * 计算科目余额
     */
    BigDecimal calculateSubjectBalance(Long subjectId, String period);
    
    /**
     * 更新科目余额
     */
    void updateAccountBalance(String period);
}