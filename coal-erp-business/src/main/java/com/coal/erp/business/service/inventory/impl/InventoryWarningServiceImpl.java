package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryWarning;
import com.coal.erp.business.mapper.inventory.InventoryWarningMapper;
import com.coal.erp.business.service.inventory.IInventoryWarningService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 库存预警服务实现
 */
@Service
public class InventoryWarningServiceImpl extends ServiceImpl<InventoryWarningMapper, InventoryWarning>
        implements IInventoryWarningService {
    
    @Override
    public Page<InventoryWarning> pageWarning(Long current, Long size, String warningType, String warningLevel, String status) {
        Page<InventoryWarning> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryWarning> wrapper = new LambdaQueryWrapper<>();
        if (warningType != null && !warningType.isEmpty()) wrapper.eq(InventoryWarning::getWarningType, warningType);
        if (warningLevel != null && !warningLevel.isEmpty()) wrapper.eq(InventoryWarning::getWarningLevel, warningLevel);
        if (status != null && !status.isEmpty()) wrapper.eq(InventoryWarning::getStatus, status);
        wrapper.orderByDesc(InventoryWarning::getWarningTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> handleWarning(Long warningId, String handleResult) {
        InventoryWarning warning = getById(warningId);
        if (warning == null) return R.error("预警不存在");
        warning.setStatus("RESOLVED");
        warning.setHandleTime(new Date());
        warning.setHandleResult(handleResult);
        warning.setUpdateTime(new Date());
        updateById(warning);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> ignoreWarning(Long warningId) {
        InventoryWarning warning = getById(warningId);
        if (warning == null) return R.error("预警不存在");
        warning.setStatus("IGNORED");
        warning.setUpdateTime(new Date());
        updateById(warning);
        return R.success();
    }
    
    @Override
    public R<?> generateWarnings() {
        // TODO: 实现预警生成逻辑
        return R.success("预警生成完成");
    }
    
    @Override
    public R<?> getWarningStatistics() {
        // TODO: 实现预警统计
        return R.success();
    }
}

