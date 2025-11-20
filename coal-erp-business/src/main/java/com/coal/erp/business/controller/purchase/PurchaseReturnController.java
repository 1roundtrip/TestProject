package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchaseReturn;
import com.coal.erp.business.domain.purchase.PurchaseReturnDetail;
import com.coal.erp.business.service.purchase.IPurchaseReturnService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购退货管理控制器
 */
@RestController
@RequestMapping("/api/purchase/return")
public class PurchaseReturnController {
    
    @Autowired
    private IPurchaseReturnService returnService;
    
    /**
     * 创建退货单
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:return:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> returnMap = (Map<String, Object>) params.get("return");
            PurchaseReturn returnOrder = convertToReturn(returnMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchaseReturnDetail> details = detailsMap.stream()
                .map(this::convertToReturnDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return returnService.createReturn(returnOrder, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'purchase:return:submit')")
    public R<?> submit(@PathVariable Long id) {
        return returnService.submitReturn(id);
    }
    
    /**
     * 审批通过
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'purchase:return:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return returnService.approveReturn(id, approveRemark);
    }
    
    /**
     * 确认退货
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'purchase:return:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return returnService.confirmReturn(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:return:list')")
    public R<Page<PurchaseReturn>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String returnNo,
            @RequestParam(required = false) String status) {
        return R.success(returnService.pageReturn(current, size, returnNo, status));
    }
    
    /**
     * 获取退货明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:return:list')")
    public R<List<PurchaseReturnDetail>> getDetails(@PathVariable Long id) {
        return R.success(returnService.getReturnDetails(id));
    }
    
    /**
     * 更新退货单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:return:edit')")
    public R<?> update(@RequestBody PurchaseReturn returnOrder) {
        return R.success(returnService.updateById(returnOrder));
    }
    
    /**
     * 删除退货单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:return:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchaseReturn returnOrder = returnService.getById(id);
        if (returnOrder != null && !"DRAFT".equals(returnOrder.getStatus())) {
            return R.error("只能删除草稿状态的退货单");
        }
        return R.success(returnService.removeById(id));
    }
    
    // 转换方法
    private PurchaseReturn convertToReturn(Map<String, Object> map) {
        PurchaseReturn returnOrder = new PurchaseReturn();
        if (map.get("receivingId") != null) returnOrder.setReceivingId(Long.valueOf(map.get("receivingId").toString()));
        if (map.get("receivingNo") != null) returnOrder.setReceivingNo(map.get("receivingNo").toString());
        if (map.get("orderId") != null) returnOrder.setOrderId(Long.valueOf(map.get("orderId").toString()));
        if (map.get("orderNo") != null) returnOrder.setOrderNo(map.get("orderNo").toString());
        if (map.get("supplierId") != null) returnOrder.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        if (map.get("supplierName") != null) returnOrder.setSupplierName(map.get("supplierName").toString());
        if (map.get("returnDate") != null) {
            try {
                returnOrder.setReturnDate(java.sql.Date.valueOf(map.get("returnDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("returnType") != null) returnOrder.setReturnType(map.get("returnType").toString());
        if (map.get("returnReason") != null) returnOrder.setReturnReason(map.get("returnReason").toString());
        if (map.get("logisticsCompany") != null) returnOrder.setLogisticsCompany(map.get("logisticsCompany").toString());
        if (map.get("logisticsNo") != null) returnOrder.setLogisticsNo(map.get("logisticsNo").toString());
        if (map.get("remark") != null) returnOrder.setRemark(map.get("remark").toString());
        return returnOrder;
    }
    
    private PurchaseReturnDetail convertToReturnDetail(Map<String, Object> map) {
        PurchaseReturnDetail detail = new PurchaseReturnDetail();
        if (map.get("receivingDetailId") != null) detail.setReceivingDetailId(Long.valueOf(map.get("receivingDetailId").toString()));
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("unit") != null) detail.setUnit(map.get("unit").toString());
        if (map.get("returnQuantity") != null) detail.setReturnQuantity(new java.math.BigDecimal(map.get("returnQuantity").toString()));
        if (map.get("unitPrice") != null) detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        if (map.get("returnReason") != null) detail.setReturnReason(map.get("returnReason").toString());
        if (map.get("batchNo") != null) detail.setBatchNo(map.get("batchNo").toString());
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

