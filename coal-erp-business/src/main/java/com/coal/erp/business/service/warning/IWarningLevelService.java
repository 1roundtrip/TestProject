package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningLevel;
import com.coal.erp.common.core.domain.R;

/**
 * 预警级别服务接口
 */
public interface IWarningLevelService extends IService<WarningLevel> {
    
    R<?> createLevel(WarningLevel level);
    
    R<?> updateLevel(WarningLevel level);
    
    Page<WarningLevel> pageLevel(Long current, Long size, String levelCode, String levelName);
}

