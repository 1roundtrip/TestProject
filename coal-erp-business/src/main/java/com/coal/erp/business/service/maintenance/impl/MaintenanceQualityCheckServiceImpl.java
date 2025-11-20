package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenanceQualityCheck;
import com.coal.erp.business.mapper.maintenance.MaintenanceQualityCheckMapper;
import com.coal.erp.business.service.maintenance.IMaintenanceQualityCheckService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 维修质量检查服务实现
 */
@Service
public class MaintenanceQualityCheckServiceImpl extends ServiceImpl<MaintenanceQualityCheckMapper, MaintenanceQualityCheck> 
        implements IMaintenanceQualityCheckService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createCheck(MaintenanceQualityCheck check) {
        try {
            if (check.getCheckNo() == null || check.getCheckNo().isEmpty()) {
                check.setCheckNo("QC" + System.currentTimeMillis());
            }
            check.setCreateTime(new Date());
            check.setUpdateTime(new Date());
            save(check);
            return R.success(check);
        } catch (Exception e) {
            return R.error("创建检查失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenanceQualityCheck> pageCheck(Long current, Long size, String checkNo, Long workOrderId) {
        Page<MaintenanceQualityCheck> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenanceQualityCheck> wrapper = new LambdaQueryWrapper<>();
        if (checkNo != null && !checkNo.isEmpty()) {
            wrapper.like(MaintenanceQualityCheck::getCheckNo, checkNo);
        }
        if (workOrderId != null) {
            wrapper.eq(MaintenanceQualityCheck::getWorkOrderId, workOrderId);
        }
        wrapper.orderByDesc(MaintenanceQualityCheck::getCheckDate);
        return page(page, wrapper);
    }
}

