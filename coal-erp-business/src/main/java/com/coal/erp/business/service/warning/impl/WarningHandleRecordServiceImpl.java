package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningHandleRecord;
import com.coal.erp.business.mapper.warning.WarningHandleRecordMapper;
import com.coal.erp.business.service.warning.IWarningHandleRecordService;
import org.springframework.stereotype.Service;

/**
 * 预警处理记录服务实现
 */
@Service
public class WarningHandleRecordServiceImpl extends ServiceImpl<WarningHandleRecordMapper, WarningHandleRecord>
        implements IWarningHandleRecordService {
    
    @Override
    public Page<WarningHandleRecord> pageHandleRecord(Long current, Long size, Long recordId, Long handlerId) {
        Page<WarningHandleRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningHandleRecord> wrapper = new LambdaQueryWrapper<>();
        
        if (recordId != null) {
            wrapper.eq(WarningHandleRecord::getRecordId, recordId);
        }
        if (handlerId != null) {
            wrapper.eq(WarningHandleRecord::getHandlerId, handlerId);
        }
        
        wrapper.orderByDesc(WarningHandleRecord::getHandleTime);
        return page(page, wrapper);
    }
}

