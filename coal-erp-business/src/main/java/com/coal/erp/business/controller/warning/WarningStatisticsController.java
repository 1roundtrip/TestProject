package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningStatistics;
import com.coal.erp.business.service.warning.IWarningStatisticsService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 预警统计控制器
 */
@RestController
@RequestMapping("/api/warning/statistics")
public class WarningStatisticsController {
    
    @Autowired
    private IWarningStatisticsService statisticsService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:statistics:list')")
    public R<Page<WarningStatistics>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String warningType,
            @RequestParam(required = false) Long warningLevelId) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startDate != null && !startDate.isEmpty() ? sdf.parse(startDate) : null;
            Date end = endDate != null && !endDate.isEmpty() ? sdf.parse(endDate) : null;
            return R.success(statisticsService.pageStatistics(current, size, start, end, warningType, warningLevelId));
        } catch (ParseException e) {
            return R.error("日期格式错误，应为 yyyy-MM-dd");
        }
    }
    
    @GetMapping("/summary")
    @PreAuthorize("hasPermission(null, 'warning:statistics:list')")
    public R<?> getSummary(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String warningType) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = startDate != null && !startDate.isEmpty() ? sdf.parse(startDate) : null;
            Date end = endDate != null && !endDate.isEmpty() ? sdf.parse(endDate) : null;
            return statisticsService.getStatisticsSummary(start, end, warningType);
        } catch (ParseException e) {
            return R.error("日期格式错误，应为 yyyy-MM-dd");
        }
    }
}

