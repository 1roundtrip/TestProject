package com.coal.erp.business.controller.finance;

import com.coal.erp.business.service.finance.IReportService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 财务报表控制器
 */
@RestController
@RequestMapping("/api/finance/report")
public class ReportController {

    @Autowired
    private IReportService reportService;

    /**
     * 生成资产负债表
     */
    @GetMapping("/balanceSheet")
    public R<Map<String, Object>> balanceSheet(@RequestParam String period) {
        return reportService.generateBalanceSheet(period);
    }

    /**
     * 生成利润表
     */
    @GetMapping("/profitStatement")
    public R<Map<String, Object>> profitStatement(@RequestParam String period) {
        return reportService.generateProfitStatement(period);
    }

    /**
     * 生成现金流量表
     */
    @GetMapping("/cashFlowStatement")
    public R<Map<String, Object>> cashFlowStatement(@RequestParam String period) {
        return reportService.generateCashFlowStatement(period);
    }

    /**
     * 导出Excel报表
     */
    @GetMapping("/export")
    public R<String> exportReport(@RequestParam String reportType, 
                                 @RequestParam String period) {
        return reportService.exportReportToExcel(reportType, period);
    }
}