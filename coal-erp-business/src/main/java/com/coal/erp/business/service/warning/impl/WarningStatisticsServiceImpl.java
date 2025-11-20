package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.business.domain.warning.WarningStatistics;
import com.coal.erp.business.mapper.warning.WarningRecordMapper;
import com.coal.erp.business.mapper.warning.WarningStatisticsMapper;
import com.coal.erp.business.service.warning.IWarningStatisticsService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 预警统计服务实现
 */
@Service
public class WarningStatisticsServiceImpl extends ServiceImpl<WarningStatisticsMapper, WarningStatistics>
        implements IWarningStatisticsService {
    
    @Autowired
    private WarningRecordMapper warningRecordMapper;
    
    @Override
    public Page<WarningStatistics> pageStatistics(Long current, Long size, Date startDate, Date endDate, String warningType, Long warningLevelId) {
        Page<WarningStatistics> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningStatistics> wrapper = new LambdaQueryWrapper<>();
        
        if (startDate != null) {
            wrapper.ge(WarningStatistics::getStatDate, startDate);
        }
        if (endDate != null) {
            wrapper.le(WarningStatistics::getStatDate, endDate);
        }
        if (warningType != null && !warningType.isEmpty()) {
            wrapper.eq(WarningStatistics::getWarningType, warningType);
        }
        if (warningLevelId != null) {
            wrapper.eq(WarningStatistics::getWarningLevelId, warningLevelId);
        }
        
        wrapper.orderByDesc(WarningStatistics::getStatDate);
        return page(page, wrapper);
    }
    
    @Override
    public R<?> getStatisticsSummary(Date startDate, Date endDate, String warningType) {
        try {
            LambdaQueryWrapper<WarningRecord> wrapper = new LambdaQueryWrapper<>();
            
            if (startDate != null) {
                wrapper.ge(WarningRecord::getTriggerTime, startDate);
            }
            if (endDate != null) {
                wrapper.le(WarningRecord::getTriggerTime, endDate);
            }
            if (warningType != null && !warningType.isEmpty()) {
                wrapper.eq(WarningRecord::getWarningType, warningType);
            }
            
            long totalCount = warningRecordMapper.selectCount(wrapper);
            
            wrapper.eq(WarningRecord::getStatus, "PENDING");
            long pendingCount = warningRecordMapper.selectCount(wrapper);
            
            wrapper.clear();
            if (startDate != null) {
                wrapper.ge(WarningRecord::getTriggerTime, startDate);
            }
            if (endDate != null) {
                wrapper.le(WarningRecord::getTriggerTime, endDate);
            }
            if (warningType != null && !warningType.isEmpty()) {
                wrapper.eq(WarningRecord::getWarningType, warningType);
            }
            wrapper.eq(WarningRecord::getStatus, "PROCESSING");
            long processingCount = warningRecordMapper.selectCount(wrapper);
            
            wrapper.clear();
            if (startDate != null) {
                wrapper.ge(WarningRecord::getTriggerTime, startDate);
            }
            if (endDate != null) {
                wrapper.le(WarningRecord::getTriggerTime, endDate);
            }
            if (warningType != null && !warningType.isEmpty()) {
                wrapper.eq(WarningRecord::getWarningType, warningType);
            }
            wrapper.eq(WarningRecord::getStatus, "RESOLVED");
            long resolvedCount = warningRecordMapper.selectCount(wrapper);
            
            wrapper.clear();
            if (startDate != null) {
                wrapper.ge(WarningRecord::getTriggerTime, startDate);
            }
            if (endDate != null) {
                wrapper.le(WarningRecord::getTriggerTime, endDate);
            }
            if (warningType != null && !warningType.isEmpty()) {
                wrapper.eq(WarningRecord::getWarningType, warningType);
            }
            wrapper.eq(WarningRecord::getStatus, "IGNORED");
            long ignoredCount = warningRecordMapper.selectCount(wrapper);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCount", totalCount);
            summary.put("pendingCount", pendingCount);
            summary.put("processingCount", processingCount);
            summary.put("resolvedCount", resolvedCount);
            summary.put("ignoredCount", ignoredCount);
            
            return R.success(summary);
        } catch (Exception e) {
            return R.error("查询失败：" + e.getMessage());
        }
    }
}

