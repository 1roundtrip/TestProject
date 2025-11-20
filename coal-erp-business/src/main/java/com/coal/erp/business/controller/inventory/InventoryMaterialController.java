package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryMaterial;
import com.coal.erp.business.service.inventory.IInventoryMaterialService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 库存物品管理控制器
 */
@RestController
@RequestMapping("/api/inventory/material")
public class InventoryMaterialController {
    
    @Autowired
    private IInventoryMaterialService materialService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:material:add')")
    public R<?> create(@RequestBody InventoryMaterial material) {
        return materialService.createMaterial(material);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'inventory:material:edit')")
    public R<?> update(@RequestBody InventoryMaterial material) {
        return materialService.updateMaterial(material);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'inventory:material:remove')")
    public R<?> delete(@PathVariable Long id) {
        materialService.removeById(id);
        return R.success();
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:material:list')")
    public R<Page<InventoryMaterial>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String materialCode,
            @RequestParam(required = false) String materialName,
            @RequestParam(required = false) String materialType,
            @RequestParam(required = false) String status) {
        return R.success(materialService.pageMaterial(current, size, materialCode, materialName, materialType, status));
    }
    
    @GetMapping("/{id}")
    public R<InventoryMaterial> getById(@PathVariable Long id) {
        return R.success(materialService.getById(id));
    }
}

