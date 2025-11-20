package com.coal.erp.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.purchase.PurchaseOrderDetail;
import com.coal.erp.business.service.IPurchaseOrderService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购订单管理控制器
 */
@RestController
@RequestMapping("/api/purchase/order")
public class PurchaseOrderController {
    
    @Autowired
    private IPurchaseOrderService purchaseOrderService;
    
    /**
     * 创建采购订单
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:order:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> orderMap = (Map<String, Object>) params.get("order");
            PurchaseOrder order = convertToOrder(orderMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchaseOrderDetail> details = detailsMap.stream()
                .map(this::convertToOrderDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return purchaseOrderService.createOrder(order, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 从采购申请创建订单
     */
    @PostMapping("/from-requisition/{requisitionId}")
    @PreAuthorize("hasPermission(null, 'purchase:order:add')")
    public R<?> createFromRequisition(@PathVariable Long requisitionId, @RequestParam Long supplierId) {
        return purchaseOrderService.createOrderFromRequisition(requisitionId, supplierId);
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'purchase:order:submit')")
    public R<?> submit(@PathVariable Long id) {
        return purchaseOrderService.submitOrder(id);
    }
    
    /**
     * 审批通过
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'purchase:order:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return purchaseOrderService.approveOrder(id, approveRemark);
    }
    
    /**
     * 确认订单
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'purchase:order:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return purchaseOrderService.confirmOrder(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:order:list')")
    public R<Page<PurchaseOrder>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long supplierId) {
        return R.success(purchaseOrderService.pageOrder(current, size, orderNo, status, supplierId));
    }
    
    /**
     * 获取订单明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:order:list')")
    public R<List<PurchaseOrderDetail>> getDetails(@PathVariable Long id) {
        return R.success(purchaseOrderService.getOrderDetails(id));
    }
    
    /**
     * 更新订单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:order:edit')")
    public R<?> update(@RequestBody PurchaseOrder order) {
        PurchaseOrder existing = purchaseOrderService.getById(order.getOrderId());
        if (existing != null && !"DRAFT".equals(existing.getStatus())) {
            return R.error("只能修改草稿状态的订单");
        }
        return R.success(purchaseOrderService.updateById(order));
    }
    
    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:order:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchaseOrder order = purchaseOrderService.getById(id);
        if (order != null && !"DRAFT".equals(order.getStatus())) {
            return R.error("只能删除草稿状态的订单");
        }
        return R.success(purchaseOrderService.removeById(id));
    }
    
    // 转换方法
    private PurchaseOrder convertToOrder(Map<String, Object> map) {
        PurchaseOrder order = new PurchaseOrder();
        if (map.get("requisitionId") != null) order.setRequisitionId(Long.valueOf(map.get("requisitionId").toString()));
        if (map.get("requisitionNo") != null) order.setRequisitionNo(map.get("requisitionNo").toString());
        if (map.get("supplierId") != null) order.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        if (map.get("supplierName") != null) order.setSupplierName(map.get("supplierName").toString());
        if (map.get("supplierCode") != null) order.setSupplierCode(map.get("supplierCode").toString());
        if (map.get("orderType") != null) order.setOrderType(map.get("orderType").toString());
        if (map.get("orderDate") != null) {
            try {
                order.setOrderDate(java.sql.Date.valueOf(map.get("orderDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("deliveryDate") != null) {
            try {
                order.setDeliveryDate(java.sql.Date.valueOf(map.get("deliveryDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("deliveryAddress") != null) order.setDeliveryAddress(map.get("deliveryAddress").toString());
        if (map.get("deliveryMethod") != null) order.setDeliveryMethod(map.get("deliveryMethod").toString());
        if (map.get("paymentTerms") != null) order.setPaymentTerms(map.get("paymentTerms").toString());
        if (map.get("currency") != null) order.setCurrency(map.get("currency").toString());
        if (map.get("buyerId") != null) order.setBuyerId(Long.valueOf(map.get("buyerId").toString()));
        if (map.get("buyerName") != null) order.setBuyerName(map.get("buyerName").toString());
        if (map.get("remark") != null) order.setRemark(map.get("remark").toString());
        return order;
    }
    
    private PurchaseOrderDetail convertToOrderDetail(Map<String, Object> map) {
        PurchaseOrderDetail detail = new PurchaseOrderDetail();
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("brand") != null) detail.setBrand(map.get("brand").toString());
        if (map.get("unit") != null) detail.setUnit(map.get("unit").toString());
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        if (map.get("unitPrice") != null) detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        if (map.get("taxRate") != null) detail.setTaxRate(new java.math.BigDecimal(map.get("taxRate").toString()));
        if (map.get("requiredDate") != null) {
            try {
                detail.setRequiredDate(java.sql.Date.valueOf(map.get("requiredDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

