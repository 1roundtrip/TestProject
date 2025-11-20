package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenanceCost;
import com.coal.erp.business.service.maintenance.IMaintenanceCostService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 维修成本控制器
 */
@RestController
@RequestMapping("/api/maintenance/cost")
public class MaintenanceCostController {
    
    @Autowired
    private IMaintenanceCostService costService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:cost:add')")
    public R<?> create(@RequestBody MaintenanceCost cost) {
        return costService.createCost(cost);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:cost:list')")
    public R<Page<MaintenanceCost>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long workOrderId,
            @RequestParam(required = false) String costType) {
        return R.success(costService.pageCost(current, size, workOrderId, costType));
    }
    
    @GetMapping("/{id}")
    public R<MaintenanceCost> getById(@PathVariable Long id) {
        return R.success(costService.getById(id));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:cost:edit')")
    public R<?> update(@RequestBody MaintenanceCost cost) {
        return R.success(costService.updateById(cost));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:cost:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(costService.removeById(id));
    }
}

