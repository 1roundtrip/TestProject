package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheck;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheckDetail;
import com.coal.erp.business.service.purchase.IPurchaseQualityCheckService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购质检管理控制器
 */
@RestController
@RequestMapping("/api/purchase/quality")
public class PurchaseQualityCheckController {
    
    @Autowired
    private IPurchaseQualityCheckService qualityCheckService;
    
    /**
     * 创建质检单
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:quality:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> checkMap = (Map<String, Object>) params.get("qualityCheck");
            PurchaseQualityCheck qualityCheck = convertToQualityCheck(checkMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchaseQualityCheckDetail> details = detailsMap.stream()
                .map(this::convertToQualityCheckDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return qualityCheckService.createQualityCheck(qualityCheck, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 从收货单创建质检单
     */
    @PostMapping("/from-receiving/{receivingId}")
    @PreAuthorize("hasPermission(null, 'purchase:quality:add')")
    public R<?> createFromReceiving(@PathVariable Long receivingId) {
        return qualityCheckService.createQualityCheckFromReceiving(receivingId);
    }
    
    /**
     * 完成质检
     */
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasPermission(null, 'purchase:quality:complete')")
    public R<?> complete(@PathVariable Long id) {
        return qualityCheckService.completeQualityCheck(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:quality:list')")
    public R<Page<PurchaseQualityCheck>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String checkNo,
            @RequestParam(required = false) String status) {
        return R.success(qualityCheckService.pageQualityCheck(current, size, checkNo, status));
    }
    
    /**
     * 获取质检明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:quality:list')")
    public R<List<PurchaseQualityCheckDetail>> getDetails(@PathVariable Long id) {
        return R.success(qualityCheckService.getQualityCheckDetails(id));
    }
    
    /**
     * 更新质检单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:quality:edit')")
    public R<?> update(@RequestBody PurchaseQualityCheck qualityCheck) {
        return R.success(qualityCheckService.updateById(qualityCheck));
    }
    
    // 转换方法
    private PurchaseQualityCheck convertToQualityCheck(Map<String, Object> map) {
        PurchaseQualityCheck qualityCheck = new PurchaseQualityCheck();
        if (map.get("receivingId") != null) qualityCheck.setReceivingId(Long.valueOf(map.get("receivingId").toString()));
        if (map.get("receivingNo") != null) qualityCheck.setReceivingNo(map.get("receivingNo").toString());
        if (map.get("orderId") != null) qualityCheck.setOrderId(Long.valueOf(map.get("orderId").toString()));
        if (map.get("orderNo") != null) qualityCheck.setOrderNo(map.get("orderNo").toString());
        if (map.get("supplierId") != null) qualityCheck.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        if (map.get("supplierName") != null) qualityCheck.setSupplierName(map.get("supplierName").toString());
        if (map.get("checkDate") != null) {
            try {
                qualityCheck.setCheckDate(java.sql.Date.valueOf(map.get("checkDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("checkType") != null) qualityCheck.setCheckType(map.get("checkType").toString());
        if (map.get("checkMethod") != null) qualityCheck.setCheckMethod(map.get("checkMethod").toString());
        if (map.get("checkStandard") != null) qualityCheck.setCheckStandard(map.get("checkStandard").toString());
        if (map.get("remark") != null) qualityCheck.setRemark(map.get("remark").toString());
        return qualityCheck;
    }
    
    private PurchaseQualityCheckDetail convertToQualityCheckDetail(Map<String, Object> map) {
        PurchaseQualityCheckDetail detail = new PurchaseQualityCheckDetail();
        if (map.get("receivingDetailId") != null) detail.setReceivingDetailId(Long.valueOf(map.get("receivingDetailId").toString()));
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("checkQuantity") != null) detail.setCheckQuantity(new java.math.BigDecimal(map.get("checkQuantity").toString()));
        if (map.get("qualifiedQuantity") != null) detail.setQualifiedQuantity(new java.math.BigDecimal(map.get("qualifiedQuantity").toString()));
        if (map.get("unqualifiedQuantity") != null) detail.setUnqualifiedQuantity(new java.math.BigDecimal(map.get("unqualifiedQuantity").toString()));
        if (map.get("checkItem") != null) detail.setCheckItem(map.get("checkItem").toString());
        if (map.get("checkResult") != null) detail.setCheckResult(map.get("checkResult").toString());
        if (map.get("defectDescription") != null) detail.setDefectDescription(map.get("defectDescription").toString());
        if (map.get("disposalMethod") != null) detail.setDisposalMethod(map.get("disposalMethod").toString());
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

