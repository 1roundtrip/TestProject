package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryLocation;
import com.coal.erp.business.service.inventory.IInventoryLocationService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 库位管理控制器
 */
@RestController
@RequestMapping("/api/inventory/location")
public class InventoryLocationController {
    
    @Autowired
    private IInventoryLocationService locationService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:location:add')")
    public R<?> create(@RequestBody InventoryLocation location) {
        return locationService.createLocation(location);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'inventory:location:edit')")
    public R<?> update(@RequestBody InventoryLocation location) {
        return locationService.updateLocation(location);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'inventory:location:remove')")
    public R<?> delete(@PathVariable Long id) {
        locationService.removeById(id);
        return R.success();
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:location:list')")
    public R<Page<InventoryLocation>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) String locationCode,
            @RequestParam(required = false) String status) {
        return R.success(locationService.pageLocation(current, size, warehouseId, locationCode, status));
    }
    
    @GetMapping("/{id}")
    public R<InventoryLocation> getById(@PathVariable Long id) {
        return R.success(locationService.getById(id));
    }
}

