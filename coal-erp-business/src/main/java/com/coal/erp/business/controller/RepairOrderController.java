package com.coal.erp.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.RepairOrder;
import com.coal.erp.business.service.IRepairOrderService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 维修中心控制器
 */
@RestController
@RequestMapping("/api/repair")
public class RepairOrderController {
    
    @Autowired
    private IRepairOrderService repairOrderService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'repair:list')")
    public R<Page<RepairOrder>> page(@RequestParam(defaultValue = "1") Long current,
                                     @RequestParam(defaultValue = "10") Long size) {
        Page<RepairOrder> page = new Page<>(current, size);
        return R.success(repairOrderService.page(page));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'repair:add')")
    public R<?> add(@RequestBody RepairOrder order) {
        return R.success(repairOrderService.save(order));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'repair:edit')")
    public R<?> update(@RequestBody RepairOrder order) {
        return R.success(repairOrderService.updateById(order));
    }
}















