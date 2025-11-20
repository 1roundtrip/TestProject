package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenancePlan;
import com.coal.erp.business.domain.maintenance.MaintenancePlanExecution;
import com.coal.erp.business.mapper.maintenance.MaintenancePlanExecutionMapper;
import com.coal.erp.business.mapper.maintenance.MaintenancePlanMapper;
import com.coal.erp.business.service.maintenance.IMaintenancePlanService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 预防性维护计划服务实现
 */
@Service
public class MaintenancePlanServiceImpl extends ServiceImpl<MaintenancePlanMapper, MaintenancePlan> 
        implements IMaintenancePlanService {
    
    @Autowired
    private MaintenancePlanExecutionMapper executionMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createPlan(MaintenancePlan plan) {
        try {
            if (plan.getPlanNo() == null || plan.getPlanNo().isEmpty()) {
                plan.setPlanNo("MP" + System.currentTimeMillis());
            }
            plan.setStatus("ACTIVE");
            plan.setCreateTime(new Date());
            plan.setUpdateTime(new Date());
            save(plan);
            return R.success(plan);
        } catch (Exception e) {
            return R.error("创建计划失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenancePlan> pagePlan(Long current, Long size, String planNo, String status, Long assetId) {
        Page<MaintenancePlan> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenancePlan> wrapper = new LambdaQueryWrapper<>();
        if (planNo != null && !planNo.isEmpty()) {
            wrapper.like(MaintenancePlan::getPlanNo, planNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MaintenancePlan::getStatus, status);
        }
        if (assetId != null) {
            wrapper.eq(MaintenancePlan::getAssetId, assetId);
        }
        wrapper.orderByDesc(MaintenancePlan::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> executePlan(Long planId) {
        try {
            MaintenancePlan plan = getById(planId);
            if (plan == null) {
                return R.error("计划不存在");
            }
            
            MaintenancePlanExecution execution = new MaintenancePlanExecution();
            execution.setPlanId(planId);
            execution.setPlanNo(plan.getPlanNo());
            execution.setScheduledDate(new Date());
            execution.setExecutionStatus("PENDING");
            execution.setCreateTime(new Date());
            executionMapper.insert(execution);
            
            return R.success(execution);
        } catch (Exception e) {
            return R.error("执行计划失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<MaintenancePlanExecution> getExecutionRecords(Long planId) {
        LambdaQueryWrapper<MaintenancePlanExecution> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaintenancePlanExecution::getPlanId, planId);
        wrapper.orderByDesc(MaintenancePlanExecution::getScheduledDate);
        return executionMapper.selectList(wrapper);
    }
}

