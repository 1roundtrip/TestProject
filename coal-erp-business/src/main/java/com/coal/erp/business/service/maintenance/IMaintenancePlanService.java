package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenancePlan;
import com.coal.erp.business.domain.maintenance.MaintenancePlanExecution;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 预防性维护计划服务接口
 */
public interface IMaintenancePlanService extends IService<MaintenancePlan> {
    
    /**
     * 创建维护计划
     */
    R<?> createPlan(MaintenancePlan plan);
    
    /**
     * 分页查询计划
     */
    Page<MaintenancePlan> pagePlan(Long current, Long size, String planNo, String status, Long assetId);
    
    /**
     * 执行计划
     */
    R<?> executePlan(Long planId);
    
    /**
     * 获取执行记录
     */
    List<MaintenancePlanExecution> getExecutionRecords(Long planId);
}

