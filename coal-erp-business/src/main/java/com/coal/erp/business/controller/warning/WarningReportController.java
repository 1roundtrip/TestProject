package com.coal.erp.business.controller.warning;

import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.business.service.warning.IWarningRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预警报表控制器
 */
@RestController
@RequestMapping("/api/warning/report")
public class WarningReportController {
    
    @Autowired
    private IWarningRecordService recordService;
    
    @GetMapping("/statistics")
    @PreAuthorize("hasPermission(null, 'warning:report:view')")
    public R<?> getStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startDate != null && !startDate.isEmpty() ? sdf.parse(startDate) : null;
            Date end = endDate != null && !endDate.isEmpty() ? sdf.parse(endDate) : null;
            
            // 获取所有记录（如果指定了日期范围，可以通过分页查询过滤）
            List<WarningRecord> allRecords = recordService.list();
            
            // 如果指定了日期范围，进行过滤
            if (start != null || end != null) {
                final Date startDateFilter = start;
                final Date endDateFilter = end;
                allRecords = allRecords.stream()
                    .filter(r -> {
                        if (r.getTriggerTime() == null) return false;
                        if (startDateFilter != null && r.getTriggerTime().before(startDateFilter)) return false;
                        if (endDateFilter != null && r.getTriggerTime().after(endDateFilter)) return false;
                        return true;
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // 按类型统计
            Map<String, Long> typeStats = allRecords.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getWarningType() != null ? r.getWarningType() : "未知",
                            Collectors.counting()
                    ));
            
            // 按级别统计
            Map<String, Long> levelStats = allRecords.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getWarningLevelCode() != null ? r.getWarningLevelCode() : "未知",
                            Collectors.counting()
                    ));
            
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", allRecords.size());
            result.put("typeStats", typeStats);
            result.put("levelStats", levelStats);
            
            return R.success(result);
        } catch (ParseException e) {
            return R.error("日期格式错误，应为 yyyy-MM-dd");
        } catch (Exception e) {
            return R.error("查询失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:report:view')")
    public R<?> getReportPage(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String warningType,
            @RequestParam(required = false) String warningLevelCode,
            @RequestParam(required = false) String status) {
        return R.success(recordService.pageRecord(current, size, warningType, warningLevelCode, status, null));
    }
}

