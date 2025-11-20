package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningRule;
import com.coal.erp.business.mapper.warning.WarningRuleMapper;
import com.coal.erp.business.service.warning.IWarningRuleService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警规则服务实现
 */
@Service
public class WarningRuleServiceImpl extends ServiceImpl<WarningRuleMapper, WarningRule>
        implements IWarningRuleService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createRule(WarningRule rule) {
        try {
            LambdaQueryWrapper<WarningRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WarningRule::getRuleCode, rule.getRuleCode());
            if (count(wrapper) > 0) {
                return R.error("规则编码已存在");
            }
            
            rule.setIsEnabled(rule.getIsEnabled() == null ? 1 : rule.getIsEnabled());
            rule.setCreateTime(new Date());
            rule.setUpdateTime(new Date());
            
            save(rule);
            return R.success(rule);
        } catch (Exception e) {
            return R.error("创建规则失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateRule(WarningRule rule) {
        try {
            rule.setUpdateTime(new Date());
            updateById(rule);
            return R.success();
        } catch (Exception e) {
            return R.error("更新规则失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<WarningRule> pageRule(Long current, Long size, String ruleCode, String ruleName, String ruleType, Integer isEnabled) {
        Page<WarningRule> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningRule> wrapper = new LambdaQueryWrapper<>();
        
        if (ruleCode != null && !ruleCode.isEmpty()) {
            wrapper.like(WarningRule::getRuleCode, ruleCode);
        }
        if (ruleName != null && !ruleName.isEmpty()) {
            wrapper.like(WarningRule::getRuleName, ruleName);
        }
        if (ruleType != null && !ruleType.isEmpty()) {
            wrapper.eq(WarningRule::getRuleType, ruleType);
        }
        if (isEnabled != null) {
            wrapper.eq(WarningRule::getIsEnabled, isEnabled);
        }
        
        wrapper.orderByDesc(WarningRule::getPriority);
        wrapper.orderByDesc(WarningRule::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> enableRule(Long ruleId, Integer isEnabled) {
        try {
            WarningRule rule = getById(ruleId);
            if (rule == null) {
                return R.error("规则不存在");
            }
            rule.setIsEnabled(isEnabled);
            rule.setUpdateTime(new Date());
            updateById(rule);
            return R.success();
        } catch (Exception e) {
            return R.error("操作失败：" + e.getMessage());
        }
    }
}

