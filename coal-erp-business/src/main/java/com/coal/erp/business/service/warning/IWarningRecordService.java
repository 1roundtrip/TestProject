package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.common.core.domain.R;

/**
 * 预警记录服务接口
 */
public interface IWarningRecordService extends IService<WarningRecord> {
    
    Page<WarningRecord> pageRecord(Long current, Long size, String warningType, String warningLevelCode, String status, String sourceType);
    
    R<?> handleRecord(Long recordId, String handleResult, Long handlerId, String handlerName);
    
    R<?> ignoreRecord(Long recordId);
    
    R<?> closeRecord(Long recordId);
}

