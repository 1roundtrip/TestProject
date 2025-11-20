package com.coal.erp.business.controller.inventory;

import com.coal.erp.business.service.inventory.IInventoryReportService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 库存报表管理控制器
 */
@RestController
@RequestMapping("/api/inventory/report")
public class InventoryReportController {
    
    @Autowired
    private IInventoryReportService reportService;
    
    @GetMapping("/stock-summary")
    @PreAuthorize("hasPermission(null, 'inventory:report:view')")
    public R<?> getStockSummary(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return reportService.getStockSummaryReport(warehouseId, startDate, endDate);
    }
    
    @GetMapping("/inbound-outbound")
    @PreAuthorize("hasPermission(null, 'inventory:report:view')")
    public R<?> getInboundOutbound(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return reportService.getInboundOutboundStatistics(warehouseId, startDate, endDate);
    }
    
    @GetMapping("/turnover-rate")
    @PreAuthorize("hasPermission(null, 'inventory:report:view')")
    public R<?> getTurnoverRate(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return reportService.getTurnoverRateAnalysis(warehouseId, startDate, endDate);
    }
    
    @GetMapping("/abc-analysis")
    @PreAuthorize("hasPermission(null, 'inventory:report:view')")
    public R<?> getABCAnalysis(@RequestParam(required = false) Long warehouseId) {
        return reportService.getABCAnalysis(warehouseId);
    }
    
    @GetMapping("/stock-value")
    @PreAuthorize("hasPermission(null, 'inventory:report:view')")
    public R<?> getStockValue(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Date asOfDate) {
        return reportService.getStockValueAnalysis(warehouseId, asOfDate);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:report:list')")
    public R<?> getReportPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String statType) {
        return reportService.getReportPage(current, size, startDate, endDate, warehouseId, statType);
    }
    
    @GetMapping("/statistics")
    @PreAuthorize("hasPermission(null, 'inventory:report:view')")
    public R<?> getReportStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) Long warehouseId) {
        return reportService.getReportStatistics(startDate, endDate, warehouseId);
    }
}

