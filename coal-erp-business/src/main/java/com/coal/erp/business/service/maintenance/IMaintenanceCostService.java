package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenanceCost;
import com.coal.erp.common.core.domain.R;

/**
 * 维修成本服务接口
 */
public interface IMaintenanceCostService extends IService<MaintenanceCost> {
    
    /**
     * 创建成本记录
     */
    R<?> createCost(MaintenanceCost cost);
    
    /**
     * 分页查询
     */
    Page<MaintenanceCost> pageCost(Long current, Long size, Long workOrderId, String costType);
}

