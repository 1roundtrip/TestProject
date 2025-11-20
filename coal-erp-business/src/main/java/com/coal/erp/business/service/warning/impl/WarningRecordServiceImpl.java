package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.business.mapper.warning.WarningRecordMapper;
import com.coal.erp.business.service.warning.IWarningRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警记录服务实现
 */
@Service
public class WarningRecordServiceImpl extends ServiceImpl<WarningRecordMapper, WarningRecord>
        implements IWarningRecordService {
    
    @Override
    public Page<WarningRecord> pageRecord(Long current, Long size, String warningType, String warningLevelCode, String status, String sourceType) {
        Page<WarningRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (warningType != null && !warningType.isEmpty()) {
            wrapper.eq(WarningRecord::getWarningType, warningType);
        }
        if (warningLevelCode != null && !warningLevelCode.isEmpty()) {
            wrapper.eq(WarningRecord::getWarningLevelCode, warningLevelCode);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(WarningRecord::getStatus, status);
        }
        if (sourceType != null && !sourceType.isEmpty()) {
            wrapper.eq(WarningRecord::getSourceType, sourceType);
        }
        
        wrapper.orderByDesc(WarningRecord::getTriggerTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> handleRecord(Long recordId, String handleResult, Long handlerId, String handlerName) {
        try {
            WarningRecord record = getById(recordId);
            if (record == null) {
                return R.error("预警记录不存在");
            }
            record.setStatus("PROCESSING");
            record.setHandlerId(handlerId);
            record.setHandlerName(handlerName);
            record.setHandleTime(new Date());
            record.setHandleResult(handleResult);
            record.setUpdateTime(new Date());
            updateById(record);
            return R.success();
        } catch (Exception e) {
            return R.error("处理失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> ignoreRecord(Long recordId) {
        try {
            WarningRecord record = getById(recordId);
            if (record == null) {
                return R.error("预警记录不存在");
            }
            record.setStatus("IGNORED");
            record.setUpdateTime(new Date());
            updateById(record);
            return R.success();
        } catch (Exception e) {
            return R.error("操作失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> closeRecord(Long recordId) {
        try {
            WarningRecord record = getById(recordId);
            if (record == null) {
                return R.error("预警记录不存在");
            }
            record.setStatus("CLOSED");
            record.setUpdateTime(new Date());
            updateById(record);
            return R.success();
        } catch (Exception e) {
            return R.error("操作失败：" + e.getMessage());
        }
    }
}

