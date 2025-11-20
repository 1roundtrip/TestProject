package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryWarning;
import com.coal.erp.business.service.inventory.IInventoryWarningService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 库存预警管理控制器
 */
@RestController
@RequestMapping("/api/inventory/warning")
public class InventoryWarningController {
    
    @Autowired
    private IInventoryWarningService warningService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:warning:list')")
    public R<Page<InventoryWarning>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String warningType,
            @RequestParam(required = false) String warningLevel,
            @RequestParam(required = false) String status) {
        return R.success(warningService.pageWarning(current, size, warningType, warningLevel, status));
    }
    
    @PostMapping("/{id}/handle")
    @PreAuthorize("hasPermission(null, 'inventory:warning:handle')")
    public R<?> handle(@PathVariable Long id, @RequestParam String handleResult) {
        return warningService.handleWarning(id, handleResult);
    }
    
    @PostMapping("/{id}/ignore")
    @PreAuthorize("hasPermission(null, 'inventory:warning:ignore')")
    public R<?> ignore(@PathVariable Long id) {
        return warningService.ignoreWarning(id);
    }
    
    @PostMapping("/generate")
    @PreAuthorize("hasPermission(null, 'inventory:warning:generate')")
    public R<?> generate() {
        return warningService.generateWarnings();
    }
    
    @GetMapping("/statistics")
    public R<?> statistics() {
        return warningService.getWarningStatistics();
    }
}

