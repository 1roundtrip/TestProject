package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningLevel;
import com.coal.erp.business.service.warning.IWarningLevelService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警级别管理控制器
 */
@RestController
@RequestMapping("/api/warning/level")
public class WarningLevelController {
    
    @Autowired
    private IWarningLevelService levelService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'warning:level:add')")
    public R<?> create(@RequestBody WarningLevel level) {
        return levelService.createLevel(level);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'warning:level:edit')")
    public R<?> update(@RequestBody WarningLevel level) {
        return levelService.updateLevel(level);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'warning:level:remove')")
    public R<?> delete(@PathVariable Long id) {
        levelService.removeById(id);
        return R.success();
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:level:list')")
    public R<Page<WarningLevel>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String levelCode,
            @RequestParam(required = false) String levelName) {
        return R.success(levelService.pageLevel(current, size, levelCode, levelName));
    }
    
    @GetMapping("/{id}")
    public R<WarningLevel> getById(@PathVariable Long id) {
        return R.success(levelService.getById(id));
    }
}

