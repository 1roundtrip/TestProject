package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningHandleRecord;

/**
 * 预警处理记录服务接口
 */
public interface IWarningHandleRecordService extends IService<WarningHandleRecord> {
    
    Page<WarningHandleRecord> pageHandleRecord(Long current, Long size, Long recordId, Long handlerId);
}

