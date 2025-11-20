package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenancePlan;
import com.coal.erp.business.service.maintenance.IMaintenancePlanService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预防性维护计划控制器
 */
@RestController
@RequestMapping("/api/maintenance/plan")
public class MaintenancePlanController {
    
    @Autowired
    private IMaintenancePlanService planService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:plan:add')")
    public R<?> create(@RequestBody MaintenancePlan plan) {
        return planService.createPlan(plan);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:plan:list')")
    public R<Page<MaintenancePlan>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String planNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long assetId) {
        return R.success(planService.pagePlan(current, size, planNo, status, assetId));
    }
    
    @GetMapping("/{id}")
    public R<MaintenancePlan> getById(@PathVariable Long id) {
        return R.success(planService.getById(id));
    }
    
    @PostMapping("/{id}/execute")
    @PreAuthorize("hasPermission(null, 'maintenance:plan:execute')")
    public R<?> execute(@PathVariable Long id) {
        return planService.executePlan(id);
    }
    
    @GetMapping("/{id}/executions")
    public R<?> getExecutions(@PathVariable Long id) {
        return R.success(planService.getExecutionRecords(id));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:plan:edit')")
    public R<?> update(@RequestBody MaintenancePlan plan) {
        return R.success(planService.updateById(plan));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:plan:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(planService.removeById(id));
    }
}

