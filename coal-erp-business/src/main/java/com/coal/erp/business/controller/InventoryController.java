package com.coal.erp.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.Inventory;
import com.coal.erp.business.service.IInventoryService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 库存中心控制器
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    @Autowired
    private IInventoryService inventoryService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:list')")
    public R<Page<Inventory>> page(@RequestParam(defaultValue = "1") Long current,
                                @RequestParam(defaultValue = "10") Long size) {
        Page<Inventory> page = new Page<>(current, size);
        return R.success(inventoryService.page(page));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:add')")
    public R<?> add(@RequestBody Inventory inventory) {
        return R.success(inventoryService.save(inventory));
    }
}















