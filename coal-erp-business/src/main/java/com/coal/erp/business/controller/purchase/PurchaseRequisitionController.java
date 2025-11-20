package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchaseRequisition;
import com.coal.erp.business.domain.purchase.PurchaseRequisitionDetail;
import com.coal.erp.business.service.purchase.IPurchaseRequisitionService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购申请管理控制器
 */
@RestController
@RequestMapping("/api/purchase/requisition")
public class PurchaseRequisitionController {
    
    @Autowired
    private IPurchaseRequisitionService requisitionService;
    
    /**
     * 创建采购申请
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:requisition:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> requisitionMap = (Map<String, Object>) params.get("requisition");
            PurchaseRequisition requisition = convertToRequisition(requisitionMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchaseRequisitionDetail> details = detailsMap.stream()
                .map(this::convertToRequisitionDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return requisitionService.createRequisition(requisition, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'purchase:requisition:submit')")
    public R<?> submit(@PathVariable Long id) {
        return requisitionService.submitRequisition(id);
    }
    
    /**
     * 审批通过
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'purchase:requisition:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return requisitionService.approveRequisition(id, approveRemark);
    }
    
    /**
     * 审批驳回
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasPermission(null, 'purchase:requisition:approve')")
    public R<?> reject(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return requisitionService.rejectRequisition(id, approveRemark);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:requisition:list')")
    public R<Page<PurchaseRequisition>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String requisitionNo,
            @RequestParam(required = false) String status) {
        return R.success(requisitionService.pageRequisition(current, size, requisitionNo, status));
    }
    
    /**
     * 获取申请明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:requisition:list')")
    public R<List<PurchaseRequisitionDetail>> getDetails(@PathVariable Long id) {
        return R.success(requisitionService.getRequisitionDetails(id));
    }
    
    /**
     * 更新申请
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:requisition:edit')")
    public R<?> update(@RequestBody PurchaseRequisition requisition) {
        return R.success(requisitionService.updateById(requisition));
    }
    
    /**
     * 删除申请
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:requisition:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchaseRequisition requisition = requisitionService.getById(id);
        if (requisition != null && !"DRAFT".equals(requisition.getStatus())) {
            return R.error("只能删除草稿状态的申请");
        }
        return R.success(requisitionService.removeById(id));
    }
    
    // 转换方法
    private PurchaseRequisition convertToRequisition(Map<String, Object> map) {
        PurchaseRequisition requisition = new PurchaseRequisition();
        if (map.get("planId") != null) requisition.setPlanId(Long.valueOf(map.get("planId").toString()));
        if (map.get("planNo") != null) requisition.setPlanNo(map.get("planNo").toString());
        if (map.get("requisitionName") != null) requisition.setRequisitionName(map.get("requisitionName").toString());
        if (map.get("deptId") != null) requisition.setDeptId(Long.valueOf(map.get("deptId").toString()));
        if (map.get("deptName") != null) requisition.setDeptName(map.get("deptName").toString());
        if (map.get("urgentLevel") != null) requisition.setUrgentLevel(map.get("urgentLevel").toString());
        if (map.get("purpose") != null) requisition.setPurpose(map.get("purpose").toString());
        if (map.get("remark") != null) requisition.setRemark(map.get("remark").toString());
        return requisition;
    }
    
    private PurchaseRequisitionDetail convertToRequisitionDetail(Map<String, Object> map) {
        PurchaseRequisitionDetail detail = new PurchaseRequisitionDetail();
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("brand") != null) detail.setBrand(map.get("brand").toString());
        if (map.get("unit") != null) detail.setUnit(map.get("unit").toString());
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        if (map.get("estimatedPrice") != null) detail.setEstimatedPrice(new java.math.BigDecimal(map.get("estimatedPrice").toString()));
        if (map.get("requiredDate") != null) {
            try {
                detail.setRequiredDate(java.sql.Date.valueOf(map.get("requiredDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("purpose") != null) detail.setPurpose(map.get("purpose").toString());
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

