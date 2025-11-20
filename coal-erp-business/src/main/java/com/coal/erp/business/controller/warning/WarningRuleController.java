package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningRule;
import com.coal.erp.business.service.warning.IWarningRuleService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警规则管理控制器
 */
@RestController
@RequestMapping("/api/warning/rule")
public class WarningRuleController {
    
    @Autowired
    private IWarningRuleService ruleService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'warning:rule:add')")
    public R<?> create(@RequestBody WarningRule rule) {
        return ruleService.createRule(rule);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'warning:rule:edit')")
    public R<?> update(@RequestBody WarningRule rule) {
        return ruleService.updateRule(rule);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'warning:rule:remove')")
    public R<?> delete(@PathVariable Long id) {
        ruleService.removeById(id);
        return R.success();
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:rule:list')")
    public R<Page<WarningRule>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String ruleCode,
            @RequestParam(required = false) String ruleName,
            @RequestParam(required = false) String ruleType,
            @RequestParam(required = false) Integer isEnabled) {
        return R.success(ruleService.pageRule(current, size, ruleCode, ruleName, ruleType, isEnabled));
    }
    
    @GetMapping("/{id}")
    public R<WarningRule> getById(@PathVariable Long id) {
        return R.success(ruleService.getById(id));
    }
    
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasPermission(null, 'warning:rule:enable')")
    public R<?> enable(@PathVariable Long id, @RequestParam Integer isEnabled) {
        return ruleService.enableRule(id, isEnabled);
    }
}

