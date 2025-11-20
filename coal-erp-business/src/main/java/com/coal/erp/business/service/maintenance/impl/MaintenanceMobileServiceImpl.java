package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenanceMobileCheckin;
import com.coal.erp.business.mapper.maintenance.MaintenanceMobileCheckinMapper;
import com.coal.erp.business.service.maintenance.IMaintenanceMobileService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 移动维修服务实现
 */
@Service
public class MaintenanceMobileServiceImpl extends ServiceImpl<MaintenanceMobileCheckinMapper, MaintenanceMobileCheckin> 
        implements IMaintenanceMobileService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> checkin(Long workOrderId, Long technicianId, String checkinType, String location, java.math.BigDecimal latitude, java.math.BigDecimal longitude) {
        try {
            MaintenanceMobileCheckin checkin = new MaintenanceMobileCheckin();
            checkin.setWorkOrderId(workOrderId);
            checkin.setTechnicianId(technicianId);
            checkin.setCheckinType(checkinType);
            checkin.setLocation(location);
            checkin.setLatitude(latitude);
            checkin.setLongitude(longitude);
            checkin.setCheckinTime(new Date());
            save(checkin);
            return R.success(checkin);
        } catch (Exception e) {
            return R.error("签到失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<MaintenanceMobileCheckin> getCheckinRecords(Long workOrderId) {
        LambdaQueryWrapper<MaintenanceMobileCheckin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaintenanceMobileCheckin::getWorkOrderId, workOrderId);
        wrapper.orderByDesc(MaintenanceMobileCheckin::getCheckinTime);
        return list(wrapper);
    }
}

