package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryInbound;
import com.coal.erp.business.domain.inventory.InventoryInboundDetail;
import com.coal.erp.business.service.inventory.IInventoryInboundService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 入库管理控制器
 */
@RestController
@RequestMapping("/api/inventory/inbound")
public class InventoryInboundController {
    
    @Autowired
    private IInventoryInboundService inboundService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:inbound:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> inboundMap = (Map<String, Object>) params.get("inbound");
            InventoryInbound inbound = convertToInbound(inboundMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<InventoryInboundDetail> details = null;
            if (detailsMap != null) {
                details = detailsMap.stream()
                    .map(this::convertToDetail)
                    .collect(java.util.stream.Collectors.toList());
            }
            
            return inboundService.createInbound(inbound, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:inbound:list')")
    public R<Page<InventoryInbound>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String inboundNo,
            @RequestParam(required = false) String inboundType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        return R.success(inboundService.pageInbound(current, size, inboundNo, inboundType, status, warehouseId));
    }
    
    @GetMapping("/{id}")
    public R<InventoryInbound> getById(@PathVariable Long id) {
        return R.success(inboundService.getById(id));
    }
    
    @GetMapping("/{id}/details")
    public R<List<InventoryInboundDetail>> getDetails(@PathVariable Long id) {
        return R.success(inboundService.getInboundDetails(id));
    }
    
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'inventory:inbound:submit')")
    public R<?> submit(@PathVariable Long id) {
        return inboundService.submitInbound(id);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'inventory:inbound:approve')")
    public R<?> approve(@PathVariable Long id) {
        return inboundService.approveInbound(id);
    }
    
    @PostMapping("/{id}/receive")
    @PreAuthorize("hasPermission(null, 'inventory:inbound:receive')")
    public R<?> receive(@PathVariable Long id) {
        return inboundService.receiveInbound(id);
    }
    
    private InventoryInbound convertToInbound(Map<String, Object> map) {
        InventoryInbound inbound = new InventoryInbound();
        if (map.get("warehouseId") != null) inbound.setWarehouseId(Long.valueOf(map.get("warehouseId").toString()));
        if (map.get("inboundType") != null) inbound.setInboundType(map.get("inboundType").toString());
        if (map.get("inboundDate") != null) inbound.setInboundDate(java.sql.Date.valueOf(map.get("inboundDate").toString()));
        if (map.get("remark") != null) inbound.setRemark(map.get("remark").toString());
        return inbound;
    }
    
    private InventoryInboundDetail convertToDetail(Map<String, Object> map) {
        InventoryInboundDetail detail = new InventoryInboundDetail();
        if (map.get("materialId") != null) detail.setMaterialId(Long.valueOf(map.get("materialId").toString()));
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        if (map.get("unitPrice") != null) detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        return detail;
    }
}

