package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryWarning;
import com.coal.erp.common.core.domain.R;

/**
 * 库存预警服务接口
 */
public interface IInventoryWarningService extends IService<InventoryWarning> {
    
    /**
     * 分页查询预警
     */
    Page<InventoryWarning> pageWarning(Long current, Long size, String warningType, String warningLevel, String status);
    
    /**
     * 处理预警
     */
    R<?> handleWarning(Long warningId, String handleResult);
    
    /**
     * 忽略预警
     */
    R<?> ignoreWarning(Long warningId);
    
    /**
     * 生成预警
     */
    R<?> generateWarnings();
    
    /**
     * 获取预警统计
     */
    R<?> getWarningStatistics();
}

