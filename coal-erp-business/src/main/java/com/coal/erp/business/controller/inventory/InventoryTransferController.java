package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryTransfer;
import com.coal.erp.business.domain.inventory.InventoryTransferDetail;
import com.coal.erp.business.service.inventory.IInventoryTransferService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 库存调拨管理控制器
 */
@RestController
@RequestMapping("/api/inventory/transfer")
public class InventoryTransferController {
    
    @Autowired
    private IInventoryTransferService transferService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:transfer:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> transferMap = (Map<String, Object>) params.get("transfer");
            InventoryTransfer transfer = new InventoryTransfer();
            if (transferMap.get("fromWarehouseId") != null) transfer.setFromWarehouseId(Long.valueOf(transferMap.get("fromWarehouseId").toString()));
            if (transferMap.get("toWarehouseId") != null) transfer.setToWarehouseId(Long.valueOf(transferMap.get("toWarehouseId").toString()));
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<InventoryTransferDetail> details = null;
            if (detailsMap != null) {
                details = detailsMap.stream()
                    .map(m -> {
                        InventoryTransferDetail d = new InventoryTransferDetail();
                        if (m.get("materialId") != null) d.setMaterialId(Long.valueOf(m.get("materialId").toString()));
                        if (m.get("quantity") != null) d.setQuantity(new java.math.BigDecimal(m.get("quantity").toString()));
                        return d;
                    })
                    .collect(java.util.stream.Collectors.toList());
            }
            return transferService.createTransfer(transfer, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:transfer:list')")
    public R<Page<InventoryTransfer>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String transferNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long fromWarehouseId,
            @RequestParam(required = false) Long toWarehouseId) {
        return R.success(transferService.pageTransfer(current, size, transferNo, status, fromWarehouseId, toWarehouseId));
    }
    
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'inventory:transfer:submit')")
    public R<?> submit(@PathVariable Long id) {
        return transferService.submitTransfer(id);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'inventory:transfer:approve')")
    public R<?> approve(@PathVariable Long id) {
        return transferService.approveTransfer(id);
    }
    
    @PostMapping("/{id}/outbound")
    @PreAuthorize("hasPermission(null, 'inventory:transfer:outbound')")
    public R<?> outbound(@PathVariable Long id) {
        return transferService.outboundTransfer(id);
    }
    
    @PostMapping("/{id}/inbound")
    @PreAuthorize("hasPermission(null, 'inventory:transfer:inbound')")
    public R<?> inbound(@PathVariable Long id) {
        return transferService.inboundTransfer(id);
    }
}

