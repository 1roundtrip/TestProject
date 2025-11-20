package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenancePerformance;
import com.coal.erp.common.core.domain.R;

/**
 * 维修绩效考核服务接口
 */
public interface IMaintenancePerformanceService extends IService<MaintenancePerformance> {
    
    /**
     * 创建考核记录
     */
    R<?> createPerformance(MaintenancePerformance performance);
    
    /**
     * 分页查询
     */
    Page<MaintenancePerformance> pagePerformance(Long current, Long size, Long userId, String evaluationPeriod);
}

