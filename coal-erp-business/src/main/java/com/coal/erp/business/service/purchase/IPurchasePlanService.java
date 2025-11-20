package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchasePlan;
import com.coal.erp.business.domain.purchase.PurchasePlanDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购计划服务接口
 */
public interface IPurchasePlanService extends IService<PurchasePlan> {
    
    /**
     * 创建采购计划
     */
    R<?> createPlan(PurchasePlan plan, List<PurchasePlanDetail> details);
    
    /**
     * 提交审批
     */
    R<?> submitPlan(Long planId);
    
    /**
     * 审批通过
     */
    R<?> approvePlan(Long planId, String approveRemark);
    
    /**
     * 审批驳回
     */
    R<?> rejectPlan(Long planId, String approveRemark);
    
    /**
     * 分页查询
     */
    Page<PurchasePlan> pagePlan(Long current, Long size, String planNo, String status, Integer planYear);
    
    /**
     * 获取计划明细
     */
    List<PurchasePlanDetail> getPlanDetails(Long planId);
}

