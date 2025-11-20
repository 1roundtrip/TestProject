package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.purchase.PurchasePlan;
import com.coal.erp.business.domain.purchase.PurchasePlanDetail;
import com.coal.erp.business.mapper.purchase.PurchasePlanDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchasePlanMapper;
import com.coal.erp.business.service.purchase.IPurchasePlanService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购计划服务实现
 */
@Service
public class PurchasePlanServiceImpl extends ServiceImpl<PurchasePlanMapper, PurchasePlan> 
        implements IPurchasePlanService {
    
    @Autowired
    private PurchasePlanDetailMapper planDetailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createPlan(PurchasePlan plan, List<PurchasePlanDetail> details) {
        // 生成计划编号
        plan.setPlanNo("JH" + System.currentTimeMillis());
        plan.setStatus("DRAFT");
        plan.setCreateUserId(SecurityUtils.getUserId());
        plan.setCreateUserName(SecurityUtils.getUsername());
        plan.setCreateTime(new Date());
        
        // 计算总金额
        BigDecimal totalAmount = details.stream()
            .map(detail -> {
                if (detail.getEstimatedPrice() != null && detail.getQuantity() != null) {
                    detail.setEstimatedAmount(detail.getEstimatedPrice()
                        .multiply(detail.getQuantity()));
                    return detail.getEstimatedAmount();
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        plan.setTotalAmount(totalAmount);
        
        // 保存计划
        save(plan);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setPlanId(plan.getPlanId());
            planDetailMapper.insert(detail);
        });
        
        return R.success(plan);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitPlan(Long planId) {
        PurchasePlan plan = getById(planId);
        if (plan == null) {
            return R.error("计划不存在");
        }
        if (!"DRAFT".equals(plan.getStatus())) {
            return R.error("只能提交草稿状态的计划");
        }
        plan.setStatus("SUBMITTED");
        plan.setUpdateTime(new Date());
        updateById(plan);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approvePlan(Long planId, String approveRemark) {
        PurchasePlan plan = getById(planId);
        if (plan == null) {
            return R.error("计划不存在");
        }
        if (!"SUBMITTED".equals(plan.getStatus())) {
            return R.error("只能审批已提交的计划");
        }
        plan.setStatus("APPROVED");
        plan.setApproveUserId(SecurityUtils.getUserId());
        plan.setApproveUserName(SecurityUtils.getUsername());
        plan.setApproveTime(new Date());
        plan.setApproveRemark(approveRemark);
        plan.setUpdateTime(new Date());
        updateById(plan);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> rejectPlan(Long planId, String approveRemark) {
        PurchasePlan plan = getById(planId);
        if (plan == null) {
            return R.error("计划不存在");
        }
        if (!"SUBMITTED".equals(plan.getStatus())) {
            return R.error("只能驳回已提交的计划");
        }
        plan.setStatus("REJECTED");
        plan.setApproveUserId(SecurityUtils.getUserId());
        plan.setApproveUserName(SecurityUtils.getUsername());
        plan.setApproveTime(new Date());
        plan.setApproveRemark(approveRemark);
        plan.setUpdateTime(new Date());
        updateById(plan);
        return R.success();
    }
    
    @Override
    public Page<PurchasePlan> pagePlan(Long current, Long size, String planNo, String status, Integer planYear) {
        Page<PurchasePlan> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchasePlan> wrapper = new LambdaQueryWrapper<>();
        if (planNo != null && !planNo.isEmpty()) {
            wrapper.like(PurchasePlan::getPlanNo, planNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchasePlan::getStatus, status);
        }
        if (planYear != null) {
            wrapper.eq(PurchasePlan::getPlanYear, planYear);
        }
        wrapper.orderByDesc(PurchasePlan::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchasePlanDetail> getPlanDetails(Long planId) {
        LambdaQueryWrapper<PurchasePlanDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchasePlanDetail::getPlanId, planId);
        return planDetailMapper.selectList(wrapper);
    }
}

