package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningRule;
import com.coal.erp.common.core.domain.R;

/**
 * 预警规则服务接口
 */
public interface IWarningRuleService extends IService<WarningRule> {
    
    R<?> createRule(WarningRule rule);
    
    R<?> updateRule(WarningRule rule);
    
    Page<WarningRule> pageRule(Long current, Long size, String ruleCode, String ruleName, String ruleType, Integer isEnabled);
    
    R<?> enableRule(Long ruleId, Integer isEnabled);
}

