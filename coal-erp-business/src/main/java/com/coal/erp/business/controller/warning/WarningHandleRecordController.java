package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningHandleRecord;
import com.coal.erp.business.service.warning.IWarningHandleRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警处理记录控制器
 */
@RestController
@RequestMapping("/api/warning/tracking")
public class WarningHandleRecordController {
    
    @Autowired
    private IWarningHandleRecordService handleRecordService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:tracking:list')")
    public R<Page<WarningHandleRecord>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long recordId,
            @RequestParam(required = false) Long handlerId) {
        return R.success(handleRecordService.pageHandleRecord(current, size, recordId, handlerId));
    }
    
    @GetMapping("/{id}")
    public R<WarningHandleRecord> getById(@PathVariable Long id) {
        return R.success(handleRecordService.getById(id));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'warning:tracking:add')")
    public R<?> create(@RequestBody WarningHandleRecord handleRecord) {
        handleRecord.setHandleTime(new java.util.Date());
        handleRecord.setCreateTime(new java.util.Date());
        boolean success = handleRecordService.save(handleRecord);
        if (success) {
            return R.success();
        } else {
            return R.error("创建失败");
        }
    }
}

