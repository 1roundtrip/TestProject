package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenanceQualityCheck;
import com.coal.erp.business.service.maintenance.IMaintenanceQualityCheckService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 维修质量检查控制器
 */
@RestController
@RequestMapping("/api/maintenance/quality")
public class MaintenanceQualityCheckController {
    
    @Autowired
    private IMaintenanceQualityCheckService qualityService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:quality:add')")
    public R<?> create(@RequestBody MaintenanceQualityCheck check) {
        return qualityService.createCheck(check);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:quality:list')")
    public R<Page<MaintenanceQualityCheck>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String checkNo,
            @RequestParam(required = false) Long workOrderId) {
        return R.success(qualityService.pageCheck(current, size, checkNo, workOrderId));
    }
    
    @GetMapping("/{id}")
    public R<MaintenanceQualityCheck> getById(@PathVariable Long id) {
        return R.success(qualityService.getById(id));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:quality:edit')")
    public R<?> update(@RequestBody MaintenanceQualityCheck check) {
        return R.success(qualityService.updateById(check));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:quality:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(qualityService.removeById(id));
    }
}

