package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenanceFaultRecord;
import com.coal.erp.common.core.domain.R;

/**
 * 设备故障记录服务接口
 */
public interface IMaintenanceFaultRecordService extends IService<MaintenanceFaultRecord> {
    
    /**
     * 创建故障记录
     */
    R<?> createFaultRecord(MaintenanceFaultRecord record);
    
    /**
     * 分页查询
     */
    Page<MaintenanceFaultRecord> pageFaultRecord(Long current, Long size, String faultNo, Long assetId, String faultType);
}

