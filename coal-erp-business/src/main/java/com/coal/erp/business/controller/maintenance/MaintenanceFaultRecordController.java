package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenanceFaultRecord;
import com.coal.erp.business.service.maintenance.IMaintenanceFaultRecordService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 设备故障记录控制器
 */
@RestController
@RequestMapping("/api/maintenance/fault")
public class MaintenanceFaultRecordController {
    
    @Autowired
    private IMaintenanceFaultRecordService faultService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:fault:add')")
    public R<?> create(@RequestBody MaintenanceFaultRecord record) {
        return faultService.createFaultRecord(record);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:fault:list')")
    public R<Page<MaintenanceFaultRecord>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String faultNo,
            @RequestParam(required = false) Long assetId,
            @RequestParam(required = false) String faultType) {
        return R.success(faultService.pageFaultRecord(current, size, faultNo, assetId, faultType));
    }
    
    @GetMapping("/{id}")
    public R<MaintenanceFaultRecord> getById(@PathVariable Long id) {
        return R.success(faultService.getById(id));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:fault:edit')")
    public R<?> update(@RequestBody MaintenanceFaultRecord record) {
        return R.success(faultService.updateById(record));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:fault:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(faultService.removeById(id));
    }
}

