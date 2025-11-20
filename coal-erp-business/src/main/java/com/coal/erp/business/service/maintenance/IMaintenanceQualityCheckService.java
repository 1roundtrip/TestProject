package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenanceQualityCheck;
import com.coal.erp.common.core.domain.R;

/**
 * 维修质量检查服务接口
 */
public interface IMaintenanceQualityCheckService extends IService<MaintenanceQualityCheck> {
    
    /**
     * 创建质量检查
     */
    R<?> createCheck(MaintenanceQualityCheck check);
    
    /**
     * 分页查询
     */
    Page<MaintenanceQualityCheck> pageCheck(Long current, Long size, String checkNo, Long workOrderId);
}

