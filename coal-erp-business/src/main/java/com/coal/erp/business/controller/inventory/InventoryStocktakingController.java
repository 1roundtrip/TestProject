package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryStocktaking;
import com.coal.erp.business.domain.inventory.InventoryStocktakingDetail;
import com.coal.erp.business.service.inventory.IInventoryStocktakingService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库存盘点管理控制器
 */
@RestController
@RequestMapping("/api/inventory/stocktaking")
public class InventoryStocktakingController {
    
    @Autowired
    private IInventoryStocktakingService stocktakingService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:stocktaking:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> stocktakingMap = (Map<String, Object>) params.get("stocktaking");
            InventoryStocktaking stocktaking = new InventoryStocktaking();
            if (stocktakingMap.get("warehouseId") != null) stocktaking.setWarehouseId(Long.valueOf(stocktakingMap.get("warehouseId").toString()));
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<InventoryStocktakingDetail> details = null;
            if (detailsMap != null) {
                details = detailsMap.stream()
                    .map(m -> {
                        InventoryStocktakingDetail d = new InventoryStocktakingDetail();
                        if (m.get("materialId") != null) d.setMaterialId(Long.valueOf(m.get("materialId").toString()));
                        return d;
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
            return stocktakingService.createStocktaking(stocktaking, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:stocktaking:list')")
    public R<Page<InventoryStocktaking>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String stocktakingNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        return R.success(stocktakingService.pageStocktaking(current, size, stocktakingNo, status, warehouseId));
    }
    
    @PostMapping("/{id}/start")
    @PreAuthorize("hasPermission(null, 'inventory:stocktaking:start')")
    public R<?> start(@PathVariable Long id) {
        return stocktakingService.startStocktaking(id);
    }
    
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasPermission(null, 'inventory:stocktaking:complete')")
    public R<?> complete(@PathVariable Long id) {
        return stocktakingService.completeStocktaking(id);
    }
    
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'inventory:stocktaking:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return stocktakingService.confirmStocktaking(id);
    }
}

