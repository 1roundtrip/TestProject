package com.coal.erp.business.controller.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.inventory.InventoryOutbound;
import com.coal.erp.business.domain.inventory.InventoryOutboundDetail;
import com.coal.erp.business.service.inventory.IInventoryOutboundService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 出库管理控制器
 */
@RestController
@RequestMapping("/api/inventory/outbound")
public class InventoryOutboundController {
    
    @Autowired
    private IInventoryOutboundService outboundService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'inventory:outbound:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> outboundMap = (Map<String, Object>) params.get("outbound");
            InventoryOutbound outbound = convertToOutbound(outboundMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<InventoryOutboundDetail> details = null;
            if (detailsMap != null) {
                details = detailsMap.stream()
                    .map(this::convertToDetail)
                    .collect(java.util.stream.Collectors.toList());
            }
            
            return outboundService.createOutbound(outbound, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'inventory:outbound:list')")
    public R<Page<InventoryOutbound>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String outboundNo,
            @RequestParam(required = false) String outboundType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long warehouseId) {
        return R.success(outboundService.pageOutbound(current, size, outboundNo, outboundType, status, warehouseId));
    }
    
    @GetMapping("/{id}")
    public R<InventoryOutbound> getById(@PathVariable Long id) {
        return R.success(outboundService.getById(id));
    }
    
    @GetMapping("/{id}/details")
    public R<List<InventoryOutboundDetail>> getDetails(@PathVariable Long id) {
        return R.success(outboundService.getOutboundDetails(id));
    }
    
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'inventory:outbound:submit')")
    public R<?> submit(@PathVariable Long id) {
        return outboundService.submitOutbound(id);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'inventory:outbound:approve')")
    public R<?> approve(@PathVariable Long id) {
        return outboundService.approveOutbound(id);
    }
    
    @PostMapping("/{id}/issue")
    @PreAuthorize("hasPermission(null, 'inventory:outbound:issue')")
    public R<?> issue(@PathVariable Long id) {
        return outboundService.issueOutbound(id);
    }
    
    private InventoryOutbound convertToOutbound(Map<String, Object> map) {
        InventoryOutbound outbound = new InventoryOutbound();
        if (map.get("warehouseId") != null) outbound.setWarehouseId(Long.valueOf(map.get("warehouseId").toString()));
        if (map.get("outboundType") != null) outbound.setOutboundType(map.get("outboundType").toString());
        if (map.get("outboundDate") != null) outbound.setOutboundDate(java.sql.Date.valueOf(map.get("outboundDate").toString()));
        return outbound;
    }
    
    private InventoryOutboundDetail convertToDetail(Map<String, Object> map) {
        InventoryOutboundDetail detail = new InventoryOutboundDetail();
        if (map.get("materialId") != null) detail.setMaterialId(Long.valueOf(map.get("materialId").toString()));
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        return detail;
    }
}

