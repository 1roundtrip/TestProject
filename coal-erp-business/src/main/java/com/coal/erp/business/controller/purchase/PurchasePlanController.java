package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchasePlan;
import com.coal.erp.business.domain.purchase.PurchasePlanDetail;
import com.coal.erp.business.service.purchase.IPurchasePlanService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购计划管理控制器
 */
@RestController
@RequestMapping("/api/purchase/plan")
public class PurchasePlanController {
    
    @Autowired
    private IPurchasePlanService planService;
    
    /**
     * 创建采购计划
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:plan:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> planMap = (Map<String, Object>) params.get("plan");
            PurchasePlan plan = convertToPlan(planMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchasePlanDetail> details = detailsMap.stream()
                .map(this::convertToPlanDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return planService.createPlan(plan, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'purchase:plan:submit')")
    public R<?> submit(@PathVariable Long id) {
        return planService.submitPlan(id);
    }
    
    /**
     * 审批通过
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'purchase:plan:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return planService.approvePlan(id, approveRemark);
    }
    
    /**
     * 审批驳回
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasPermission(null, 'purchase:plan:approve')")
    public R<?> reject(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return planService.rejectPlan(id, approveRemark);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:plan:list')")
    public R<Page<PurchasePlan>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String planNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer planYear) {
        return R.success(planService.pagePlan(current, size, planNo, status, planYear));
    }
    
    /**
     * 获取计划明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:plan:list')")
    public R<List<PurchasePlanDetail>> getDetails(@PathVariable Long id) {
        return R.success(planService.getPlanDetails(id));
    }
    
    /**
     * 更新计划
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:plan:edit')")
    public R<?> update(@RequestBody PurchasePlan plan) {
        return R.success(planService.updateById(plan));
    }
    
    /**
     * 删除计划
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:plan:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchasePlan plan = planService.getById(id);
        if (plan != null && !"DRAFT".equals(plan.getStatus())) {
            return R.error("只能删除草稿状态的计划");
        }
        return R.success(planService.removeById(id));
    }
    
    // 转换方法
    private PurchasePlan convertToPlan(Map<String, Object> map) {
        PurchasePlan plan = new PurchasePlan();
        if (map.get("planName") != null) plan.setPlanName(map.get("planName").toString());
        if (map.get("planYear") != null) plan.setPlanYear(Integer.valueOf(map.get("planYear").toString()));
        if (map.get("planQuarter") != null) plan.setPlanQuarter(Integer.valueOf(map.get("planQuarter").toString()));
        if (map.get("planMonth") != null) plan.setPlanMonth(Integer.valueOf(map.get("planMonth").toString()));
        if (map.get("deptId") != null) plan.setDeptId(Long.valueOf(map.get("deptId").toString()));
        if (map.get("deptName") != null) plan.setDeptName(map.get("deptName").toString());
        if (map.get("budgetAmount") != null) plan.setBudgetAmount(new java.math.BigDecimal(map.get("budgetAmount").toString()));
        if (map.get("remark") != null) plan.setRemark(map.get("remark").toString());
        return plan;
    }
    
    private PurchasePlanDetail convertToPlanDetail(Map<String, Object> map) {
        PurchasePlanDetail detail = new PurchasePlanDetail();
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("unit") != null) detail.setUnit(map.get("unit").toString());
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        if (map.get("estimatedPrice") != null) detail.setEstimatedPrice(new java.math.BigDecimal(map.get("estimatedPrice").toString()));
        if (map.get("purpose") != null) detail.setPurpose(map.get("purpose").toString());
        if (map.get("requiredDate") != null) {
            try {
                detail.setRequiredDate(java.sql.Date.valueOf(map.get("requiredDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("priority") != null) detail.setPriority(map.get("priority").toString());
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

