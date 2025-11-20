package com.coal.erp.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.WarningAlert;
import com.coal.erp.business.service.IWarningAlertService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预警中心控制器（旧版预警系统，用于兼容）
 * 注意：新的预警管理系统使用 /api/warning/rule, /api/warning/monitor 等路径
 */
@RestController
@RequestMapping("/api/warning/alert")
public class WarningAlertController {
    
    @Autowired
    private IWarningAlertService warningAlertService;
    
    /**
     * 分页查询预警记录
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户访问
     */
    @GetMapping("/page")
    // @PreAuthorize("hasPermission(null, 'warning:list')")
    public R<Page<WarningAlert>> page(@RequestParam(defaultValue = "1") Long current,
                                       @RequestParam(defaultValue = "10") Long size,
                                       @RequestParam(required = false) String alertLevel,
                                       @RequestParam(required = false) String status) {
        Page<WarningAlert> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningAlert> wrapper = new LambdaQueryWrapper<>();
        
        if (alertLevel != null && !alertLevel.isEmpty()) {
            wrapper.eq(WarningAlert::getAlertLevel, alertLevel);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(WarningAlert::getStatus, status);
        }
        
        wrapper.orderByDesc(WarningAlert::getCreateTime);
        return R.success(warningAlertService.page(page, wrapper));
    }
    
    /**
     * 根据级别查询预警
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户访问
     */
    @GetMapping("/level/{level}")
    // @PreAuthorize("hasPermission(null, 'warning:query')")
    public R<List<WarningAlert>> getByLevel(@PathVariable String level) {
        List<WarningAlert> alerts = warningAlertService.getAlertsByLevel(level);
        return R.success(alerts);
    }
    
    /**
     * 获取预警统计
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户访问
     */
    @GetMapping("/stats")
    // @PreAuthorize("hasPermission(null, 'warning:query')")
    public R<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        Long totalUnhandled = warningAlertService.getUnhandledAlertCount();
        Long yellowCount = (long) warningAlertService.getAlertsByLevel("YELLOW").size();
        Long orangeCount = (long) warningAlertService.getAlertsByLevel("ORANGE").size();
        Long redCount = (long) warningAlertService.getAlertsByLevel("RED").size();
        
        stats.put("totalUnhandled", totalUnhandled);
        stats.put("yellowCount", yellowCount);
        stats.put("orangeCount", orangeCount);
        stats.put("redCount", redCount);
        
        return R.success(stats);
    }
    
    /**
     * 标记预警为已处理
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户访问
     */
    @PutMapping("/{id}/handle")
    // @PreAuthorize("hasPermission(null, 'warning:handle')")
    public R<?> handleAlert(@PathVariable Long id) {
        warningAlertService.markAsHandled(id);
        return R.success();
    }
    
    /**
     * 批量处理预警
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户访问
     */
    @PutMapping("/batch-handle")
    // @PreAuthorize("hasPermission(null, 'warning:handle')")
    public R<?> batchHandle(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            warningAlertService.markAsHandled(id);
        }
        return R.success();
    }
}





