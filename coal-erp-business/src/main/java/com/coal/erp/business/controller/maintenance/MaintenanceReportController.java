package com.coal.erp.business.controller.maintenance;

import com.coal.erp.business.service.maintenance.IMaintenanceReportService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 维修报表控制器
 */
@RestController
@RequestMapping("/api/maintenance/report")
public class MaintenanceReportController {
    
    @Autowired
    private IMaintenanceReportService reportService;
    
    @GetMapping("/statistics")
    @PreAuthorize("hasPermission(null, 'maintenance:report:view')")
    public R<Map<String, Object>> getStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return reportService.getStatistics(startDate, endDate);
    }
    
    @GetMapping("/work-order")
    @PreAuthorize("hasPermission(null, 'maintenance:report:view')")
    public R<Map<String, Object>> getWorkOrderStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return reportService.getWorkOrderStatistics(startDate, endDate);
    }
    
    @GetMapping("/cost")
    @PreAuthorize("hasPermission(null, 'maintenance:report:view')")
    public R<Map<String, Object>> getCostStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return reportService.getCostStatistics(startDate, endDate);
    }
    
    @GetMapping("/fault")
    @PreAuthorize("hasPermission(null, 'maintenance:report:view')")
    public R<Map<String, Object>> getFaultStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return reportService.getFaultStatistics(startDate, endDate);
    }
}

