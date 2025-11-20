package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchaseReceiving;
import com.coal.erp.business.domain.purchase.PurchaseReceivingDetail;
import com.coal.erp.business.service.purchase.IPurchaseReceivingService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购收货管理控制器
 */
@RestController
@RequestMapping("/api/purchase/receiving")
public class PurchaseReceivingController {
    
    @Autowired
    private IPurchaseReceivingService receivingService;
    
    /**
     * 创建收货单
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:receiving:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> receivingMap = (Map<String, Object>) params.get("receiving");
            PurchaseReceiving receiving = convertToReceiving(receivingMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchaseReceivingDetail> details = detailsMap.stream()
                .map(this::convertToReceivingDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return receivingService.createReceiving(receiving, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 从采购订单创建收货单
     */
    @PostMapping("/from-order/{orderId}")
    @PreAuthorize("hasPermission(null, 'purchase:receiving:add')")
    public R<?> createFromOrder(@PathVariable Long orderId) {
        return receivingService.createReceivingFromOrder(orderId);
    }
    
    /**
     * 确认收货
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'purchase:receiving:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return receivingService.confirmReceiving(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:receiving:list')")
    public R<Page<PurchaseReceiving>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String receivingNo,
            @RequestParam(required = false) String status) {
        return R.success(receivingService.pageReceiving(current, size, receivingNo, status));
    }
    
    /**
     * 获取收货明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:receiving:list')")
    public R<List<PurchaseReceivingDetail>> getDetails(@PathVariable Long id) {
        return R.success(receivingService.getReceivingDetails(id));
    }
    
    /**
     * 更新收货单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:receiving:edit')")
    public R<?> update(@RequestBody PurchaseReceiving receiving) {
        return R.success(receivingService.updateById(receiving));
    }
    
    /**
     * 删除收货单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:receiving:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchaseReceiving receiving = receivingService.getById(id);
        if (receiving != null && !"DRAFT".equals(receiving.getStatus())) {
            return R.error("只能删除草稿状态的收货单");
        }
        return R.success(receivingService.removeById(id));
    }
    
    // 转换方法
    private PurchaseReceiving convertToReceiving(Map<String, Object> map) {
        PurchaseReceiving receiving = new PurchaseReceiving();
        if (map.get("orderId") != null) receiving.setOrderId(Long.valueOf(map.get("orderId").toString()));
        if (map.get("orderNo") != null) receiving.setOrderNo(map.get("orderNo").toString());
        if (map.get("contractId") != null) receiving.setContractId(Long.valueOf(map.get("contractId").toString()));
        if (map.get("contractNo") != null) receiving.setContractNo(map.get("contractNo").toString());
        if (map.get("supplierId") != null) receiving.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        if (map.get("supplierName") != null) receiving.setSupplierName(map.get("supplierName").toString());
        if (map.get("receivingDate") != null) {
            try {
                receiving.setReceivingDate(java.sql.Date.valueOf(map.get("receivingDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("warehouse") != null) receiving.setWarehouse(map.get("warehouse").toString());
        if (map.get("location") != null) receiving.setLocation(map.get("location").toString());
        if (map.get("deliveryNo") != null) receiving.setDeliveryNo(map.get("deliveryNo").toString());
        if (map.get("logisticsCompany") != null) receiving.setLogisticsCompany(map.get("logisticsCompany").toString());
        if (map.get("logisticsNo") != null) receiving.setLogisticsNo(map.get("logisticsNo").toString());
        if (map.get("remark") != null) receiving.setRemark(map.get("remark").toString());
        return receiving;
    }
    
    private PurchaseReceivingDetail convertToReceivingDetail(Map<String, Object> map) {
        PurchaseReceivingDetail detail = new PurchaseReceivingDetail();
        if (map.get("orderDetailId") != null) detail.setOrderDetailId(Long.valueOf(map.get("orderDetailId").toString()));
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("unit") != null) detail.setUnit(map.get("unit").toString());
        if (map.get("orderQuantity") != null) detail.setOrderQuantity(new java.math.BigDecimal(map.get("orderQuantity").toString()));
        if (map.get("receivedQuantity") != null) detail.setReceivedQuantity(new java.math.BigDecimal(map.get("receivedQuantity").toString()));
        if (map.get("unitPrice") != null) detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        if (map.get("batchNo") != null) detail.setBatchNo(map.get("batchNo").toString());
        if (map.get("productionDate") != null) {
            try {
                detail.setProductionDate(java.sql.Date.valueOf(map.get("productionDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("expiryDate") != null) {
            try {
                detail.setExpiryDate(java.sql.Date.valueOf(map.get("expiryDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

