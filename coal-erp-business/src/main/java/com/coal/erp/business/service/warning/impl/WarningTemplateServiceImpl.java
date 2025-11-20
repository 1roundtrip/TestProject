package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningTemplate;
import com.coal.erp.business.mapper.warning.WarningTemplateMapper;
import com.coal.erp.business.service.warning.IWarningTemplateService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警模板服务实现
 */
@Service
public class WarningTemplateServiceImpl extends ServiceImpl<WarningTemplateMapper, WarningTemplate>
        implements IWarningTemplateService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createTemplate(WarningTemplate template) {
        try {
            LambdaQueryWrapper<WarningTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WarningTemplate::getTemplateCode, template.getTemplateCode());
            if (count(wrapper) > 0) {
                return R.error("模板编码已存在");
            }
            
            template.setIsEnabled(template.getIsEnabled() == null ? 1 : template.getIsEnabled());
            template.setIsDefault(template.getIsDefault() == null ? 0 : template.getIsDefault());
            template.setCreateTime(new Date());
            template.setUpdateTime(new Date());
            
            save(template);
            return R.success(template);
        } catch (Exception e) {
            return R.error("创建模板失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateTemplate(WarningTemplate template) {
        try {
            template.setUpdateTime(new Date());
            updateById(template);
            return R.success();
        } catch (Exception e) {
            return R.error("更新模板失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<WarningTemplate> pageTemplate(Long current, Long size, String templateCode, String templateName, String templateType) {
        Page<WarningTemplate> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningTemplate> wrapper = new LambdaQueryWrapper<>();
        
        if (templateCode != null && !templateCode.isEmpty()) {
            wrapper.like(WarningTemplate::getTemplateCode, templateCode);
        }
        if (templateName != null && !templateName.isEmpty()) {
            wrapper.like(WarningTemplate::getTemplateName, templateName);
        }
        if (templateType != null && !templateType.isEmpty()) {
            wrapper.eq(WarningTemplate::getTemplateType, templateType);
        }
        
        wrapper.orderByDesc(WarningTemplate::getIsDefault);
        wrapper.orderByDesc(WarningTemplate::getCreateTime);
        return page(page, wrapper);
    }
}

