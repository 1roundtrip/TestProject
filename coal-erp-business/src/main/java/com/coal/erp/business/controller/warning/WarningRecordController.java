package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.business.service.warning.IWarningRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 预警监控（记录）控制器
 */
@RestController
@RequestMapping("/api/warning/monitor")
public class WarningRecordController {
    
    @Autowired
    private IWarningRecordService recordService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:monitor:list')")
    public R<Page<WarningRecord>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String warningType,
            @RequestParam(required = false) String warningLevelCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sourceType) {
        return R.success(recordService.pageRecord(current, size, warningType, warningLevelCode, status, sourceType));
    }
    
    @GetMapping("/{id}")
    public R<WarningRecord> getById(@PathVariable Long id) {
        return R.success(recordService.getById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'warning:monitor:add')")
    public R<?> create(@RequestBody WarningRecord record) {
        record.setStatus("PENDING");
        record.setTriggerTime(new java.util.Date());
        record.setCreateTime(new java.util.Date());
        record.setUpdateTime(new java.util.Date());
        boolean success = recordService.save(record);
        if (success) {
            return R.success();
        } else {
            return R.error("创建失败");
        }
    }
    
    @PostMapping("/{id}/handle")
    @PreAuthorize("hasPermission(null, 'warning:monitor:handle')")
    public R<?> handle(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String handleResult = (String) params.get("handleResult");
        Long handlerId = params.get("handlerId") != null ? Long.valueOf(params.get("handlerId").toString()) : null;
        String handlerName = (String) params.get("handlerName");
        return recordService.handleRecord(id, handleResult, handlerId, handlerName);
    }
    
    @PostMapping("/{id}/ignore")
    @PreAuthorize("hasPermission(null, 'warning:monitor:ignore')")
    public R<?> ignore(@PathVariable Long id) {
        return recordService.ignoreRecord(id);
    }
    
    @PostMapping("/{id}/close")
    @PreAuthorize("hasPermission(null, 'warning:monitor:close')")
    public R<?> close(@PathVariable Long id) {
        return recordService.closeRecord(id);
    }
}

