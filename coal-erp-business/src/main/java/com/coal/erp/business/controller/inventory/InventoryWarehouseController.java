package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryWarehouse;
import com.coal.erp.business.service.inventory.IInventoryWarehouseService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 仓库管理控制器
 */
@RestController
@RequestMapping("/api/inventory/warehouse")
public class InventoryWarehouseController {
    
    @Autowired
    private IInventoryWarehouseService warehouseService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:warehouse:add')")
    public R<?> create(@RequestBody InventoryWarehouse warehouse) {
        return warehouseService.createWarehouse(warehouse);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'inventory:warehouse:edit')")
    public R<?> update(@RequestBody InventoryWarehouse warehouse) {
        return warehouseService.updateWarehouse(warehouse);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'inventory:warehouse:remove')")
    public R<?> delete(@PathVariable Long id) {
        warehouseService.removeById(id);
        return R.success();
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:warehouse:list')")
    public R<Page<InventoryWarehouse>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String warehouseCode,
            @RequestParam(required = false) String warehouseName,
            @RequestParam(required = false) String status) {
        return R.success(warehouseService.pageWarehouse(current, size, warehouseCode, warehouseName, status));
    }
    
    @GetMapping("/{id}")
    public R<InventoryWarehouse> getById(@PathVariable Long id) {
        return R.success(warehouseService.getById(id));
    }
}

