package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenanceMobileCheckin;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 移动维修服务接口
 */
public interface IMaintenanceMobileService extends IService<MaintenanceMobileCheckin> {
    
    /**
     * 签到
     */
    R<?> checkin(Long workOrderId, Long technicianId, String checkinType, String location, java.math.BigDecimal latitude, java.math.BigDecimal longitude);
    
    /**
     * 获取签到记录
     */
    List<MaintenanceMobileCheckin> getCheckinRecords(Long workOrderId);
}

