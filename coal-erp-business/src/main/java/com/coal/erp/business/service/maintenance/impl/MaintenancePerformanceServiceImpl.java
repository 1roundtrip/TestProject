package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenancePerformance;
import com.coal.erp.business.mapper.maintenance.MaintenancePerformanceMapper;
import com.coal.erp.business.service.maintenance.IMaintenancePerformanceService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 维修绩效考核服务实现
 */
@Service
public class MaintenancePerformanceServiceImpl extends ServiceImpl<MaintenancePerformanceMapper, MaintenancePerformance> 
        implements IMaintenancePerformanceService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createPerformance(MaintenancePerformance performance) {
        try {
            performance.setCreateTime(new Date());
            performance.setUpdateTime(new Date());
            save(performance);
            return R.success(performance);
        } catch (Exception e) {
            return R.error("创建考核记录失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenancePerformance> pagePerformance(Long current, Long size, Long userId, String evaluationPeriod) {
        Page<MaintenancePerformance> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenancePerformance> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(MaintenancePerformance::getEvaluatedUserId, userId);
        }
        if (evaluationPeriod != null && !evaluationPeriod.isEmpty()) {
            wrapper.eq(MaintenancePerformance::getEvaluationPeriod, evaluationPeriod);
        }
        wrapper.orderByDesc(MaintenancePerformance::getEvaluationDate);
        return page(page, wrapper);
    }
}

