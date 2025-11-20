package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenanceFaultRecord;
import com.coal.erp.business.mapper.maintenance.MaintenanceFaultRecordMapper;
import com.coal.erp.business.service.maintenance.IMaintenanceFaultRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 设备故障记录服务实现
 */
@Service
public class MaintenanceFaultRecordServiceImpl extends ServiceImpl<MaintenanceFaultRecordMapper, MaintenanceFaultRecord> 
        implements IMaintenanceFaultRecordService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createFaultRecord(MaintenanceFaultRecord record) {
        try {
            if (record.getFaultNo() == null || record.getFaultNo().isEmpty()) {
                record.setFaultNo("FR" + System.currentTimeMillis());
            }
            if (record.getOccurredTime() == null) {
                record.setOccurredTime(new Date());
            }
            if (record.getReportedTime() == null) {
                record.setReportedTime(new Date());
            }
            record.setCreateTime(new Date());
            record.setUpdateTime(new Date());
            save(record);
            return R.success(record);
        } catch (Exception e) {
            return R.error("创建故障记录失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenanceFaultRecord> pageFaultRecord(Long current, Long size, String faultNo, Long assetId, String faultType) {
        Page<MaintenanceFaultRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenanceFaultRecord> wrapper = new LambdaQueryWrapper<>();
        if (faultNo != null && !faultNo.isEmpty()) {
            wrapper.like(MaintenanceFaultRecord::getFaultNo, faultNo);
        }
        if (assetId != null) {
            wrapper.eq(MaintenanceFaultRecord::getAssetId, assetId);
        }
        if (faultType != null && !faultType.isEmpty()) {
            wrapper.eq(MaintenanceFaultRecord::getFaultType, faultType);
        }
        wrapper.orderByDesc(MaintenanceFaultRecord::getOccurredTime);
        return page(page, wrapper);
    }
}

