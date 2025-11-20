package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenancePerformance;
import com.coal.erp.business.service.maintenance.IMaintenancePerformanceService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 维修绩效考核控制器
 */
@RestController
@RequestMapping("/api/maintenance/performance")
public class MaintenancePerformanceController {
    
    @Autowired
    private IMaintenancePerformanceService performanceService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:performance:add')")
    public R<?> create(@RequestBody MaintenancePerformance performance) {
        return performanceService.createPerformance(performance);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:performance:list')")
    public R<Page<MaintenancePerformance>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String evaluationPeriod) {
        return R.success(performanceService.pagePerformance(current, size, userId, evaluationPeriod));
    }
    
    @GetMapping("/{id}")
    public R<MaintenancePerformance> getById(@PathVariable Long id) {
        return R.success(performanceService.getById(id));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:performance:edit')")
    public R<?> update(@RequestBody MaintenancePerformance performance) {
        return R.success(performanceService.updateById(performance));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:performance:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(performanceService.removeById(id));
    }
}

