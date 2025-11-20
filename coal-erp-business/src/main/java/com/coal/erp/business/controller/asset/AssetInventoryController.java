package com.coal.erp.business.controller.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.asset.AssetInventory;
import com.coal.erp.business.domain.asset.AssetInventoryDetail;
import com.coal.erp.business.service.asset.IAssetInventoryService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 资产盘点管理控制器
 */
@RestController
@RequestMapping("/api/asset/inventory")
public class AssetInventoryController {
    
    @Autowired
    private IAssetInventoryService inventoryService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'asset:inventory:add')")
    public R<?> create(@RequestBody AssetInventory inventory) {
        return inventoryService.createInventory(inventory);
    }
    
    @PostMapping("/{id}/details")
    public R<?> addDetails(@PathVariable Long id, @RequestBody List<AssetInventoryDetail> details) {
        return inventoryService.addInventoryDetail(id, details);
    }
    
    @PostMapping("/{id}/start")
    public R<?> start(@PathVariable Long id) {
        return inventoryService.startInventory(id);
    }
    
    @PostMapping("/{id}/complete")
    public R<?> complete(@PathVariable Long id) {
        return inventoryService.completeInventory(id);
    }
    
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'asset:inventory:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return inventoryService.confirmInventory(id);
    }
    
    @PostMapping("/detail/{detailId}/handle")
    public R<?> handleDifference(@PathVariable Long detailId, @RequestBody Map<String, String> params) {
        String handleRemark = params.get("handleRemark");
        return inventoryService.handleDifference(detailId, handleRemark);
    }
    
    @GetMapping("/page")
    public R<Page<AssetInventory>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String inventoryNo,
            @RequestParam(required = false) String status) {
        return R.success(inventoryService.pageInventory(current, size, inventoryNo, status));
    }
    
    @GetMapping("/{id}")
    public R<AssetInventory> getById(@PathVariable Long id) {
        return R.success(inventoryService.getById(id));
    }
    
    @GetMapping("/{id}/details")
    public R<List<AssetInventoryDetail>> getDetails(@PathVariable Long id) {
        return R.success(inventoryService.getInventoryDetails(id));
    }
    
    @PutMapping
    public R<?> update(@RequestBody AssetInventory inventory) {
        return R.success(inventoryService.updateById(inventory));
    }
    
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        return R.success(inventoryService.removeById(id));
    }
}

