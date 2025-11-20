package com.coal.erp.business.service.finance;

import com.coal.erp.common.core.domain.R;

import java.util.Map;

/**
 * 财务报表服务接口
 */
public interface IReportService {
    /**
     * 生成资产负债表
     */
    R<Map<String, Object>> generateBalanceSheet(String period);
    
    /**
     * 生成利润表
     */
    R<Map<String, Object>> generateProfitStatement(String period);
    
    /**
     * 生成现金流量表
     */
    R<Map<String, Object>> generateCashFlowStatement(String period);
    
    /**
     * 导出Excel报表
     */
    R<String> exportReportToExcel(String reportType, String period);
}