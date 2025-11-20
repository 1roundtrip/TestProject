package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.business.service.warning.IWarningRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预警看板控制器
 */
@RestController
@RequestMapping("/api/warning/dashboard")
public class WarningDashboardController {
    
    @Autowired
    private IWarningRecordService recordService;
    
    @GetMapping("/summary")
    @PreAuthorize("hasPermission(null, 'warning:dashboard:view')")
    public R<?> getSummary() {
        try {
            // 获取所有预警记录进行统计
            List<WarningRecord> allRecords = recordService.list();
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCount", allRecords.size());
            summary.put("pendingCount", allRecords.stream().filter(r -> "PENDING".equals(r.getStatus())).count());
            summary.put("processingCount", allRecords.stream().filter(r -> "PROCESSING".equals(r.getStatus())).count());
            summary.put("resolvedCount", allRecords.stream().filter(r -> "RESOLVED".equals(r.getStatus())).count());
            summary.put("criticalCount", allRecords.stream().filter(r -> "CRITICAL".equals(r.getWarningLevelCode())).count());
            summary.put("highCount", allRecords.stream().filter(r -> "HIGH".equals(r.getWarningLevelCode())).count());
            
            return R.success(summary);
        } catch (Exception e) {
            return R.error("查询失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/recent")
    @PreAuthorize("hasPermission(null, 'warning:dashboard:view')")
    public R<?> getRecent(@RequestParam(defaultValue = "10") Integer limit) {
        try {
            Page<WarningRecord> page = recordService.pageRecord(1L, limit.longValue(), null, null, null, null);
            return R.success(page.getRecords());
        } catch (Exception e) {
            return R.error("查询失败：" + e.getMessage());
        }
    }
}

