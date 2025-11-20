package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryStock;
import com.coal.erp.business.service.inventory.IInventoryStockService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 库存明细管理控制器
 */
@RestController
@RequestMapping("/api/inventory/stock")
public class InventoryStockController {
    
    @Autowired
    private IInventoryStockService stockService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:stock:list')")
    public R<Page<InventoryStock>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long locationId,
            @RequestParam(required = false) String materialCode,
            @RequestParam(required = false) String materialName) {
        return R.success(stockService.pageStock(current, size, warehouseId, locationId, materialCode, materialName));
    }
    
    @GetMapping("/material/{materialId}/summary")
    public R<?> getMaterialSummary(@PathVariable Long materialId) {
        return stockService.getMaterialStockSummary(materialId);
    }
    
    @GetMapping("/warehouse/{warehouseId}/summary")
    public R<?> getWarehouseSummary(@PathVariable Long warehouseId) {
        return stockService.getWarehouseStockSummary(warehouseId);
    }
}

