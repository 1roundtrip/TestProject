package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryAdjustment;
import com.coal.erp.business.domain.inventory.InventoryAdjustmentDetail;
import com.coal.erp.business.service.inventory.IInventoryAdjustmentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库存调整管理控制器
 */
@RestController
@RequestMapping("/api/inventory/adjustment")
public class InventoryAdjustmentController {
    
    @Autowired
    private IInventoryAdjustmentService adjustmentService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:adjustment:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> adjustmentMap = (Map<String, Object>) params.get("adjustment");
            InventoryAdjustment adjustment = new InventoryAdjustment();
            if (adjustmentMap.get("warehouseId") != null) adjustment.setWarehouseId(Long.valueOf(adjustmentMap.get("warehouseId").toString()));
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<InventoryAdjustmentDetail> details = null;
            if (detailsMap != null) {
                details = detailsMap.stream()
                    .map(m -> {
                        InventoryAdjustmentDetail d = new InventoryAdjustmentDetail();
                        if (m.get("materialId") != null) d.setMaterialId(Long.valueOf(m.get("materialId").toString()));
                        return d;
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
            return adjustmentService.createAdjustment(adjustment, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:adjustment:list')")
    public R<Page<InventoryAdjustment>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String adjustmentNo,
            @RequestParam(required = false) String adjustmentType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        return R.success(adjustmentService.pageAdjustment(current, size, adjustmentNo, adjustmentType, status, warehouseId));
    }
    
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'inventory:adjustment:submit')")
    public R<?> submit(@PathVariable Long id) {
        return adjustmentService.submitAdjustment(id);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'inventory:adjustment:approve')")
    public R<?> approve(@PathVariable Long id) {
        return adjustmentService.approveAdjustment(id);
    }
}

