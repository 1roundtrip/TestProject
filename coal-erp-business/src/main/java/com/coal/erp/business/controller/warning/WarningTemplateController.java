package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningTemplate;
import com.coal.erp.business.service.warning.IWarningTemplateService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警模板管理控制器
 */
@RestController
@RequestMapping("/api/warning/template")
public class WarningTemplateController {
    
    @Autowired
    private IWarningTemplateService templateService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'warning:template:add')")
    public R<?> create(@RequestBody WarningTemplate template) {
        return templateService.createTemplate(template);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'warning:template:edit')")
    public R<?> update(@RequestBody WarningTemplate template) {
        return templateService.updateTemplate(template);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'warning:template:remove')")
    public R<?> delete(@PathVariable Long id) {
        templateService.removeById(id);
        return R.success();
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:template:list')")
    public R<Page<WarningTemplate>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String templateCode,
            @RequestParam(required = false) String templateName,
            @RequestParam(required = false) String templateType) {
        return R.success(templateService.pageTemplate(current, size, templateCode, templateName, templateType));
    }
    
    @GetMapping("/{id}")
    public R<WarningTemplate> getById(@PathVariable Long id) {
        return R.success(templateService.getById(id));
    }
}

