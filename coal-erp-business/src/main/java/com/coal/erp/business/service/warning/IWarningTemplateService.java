package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningTemplate;
import com.coal.erp.common.core.domain.R;

/**
 * 预警模板服务接口
 */
public interface IWarningTemplateService extends IService<WarningTemplate> {
    
    R<?> createTemplate(WarningTemplate template);
    
    R<?> updateTemplate(WarningTemplate template);
    
    Page<WarningTemplate> pageTemplate(Long current, Long size, String templateCode, String templateName, String templateType);
}

