package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenanceCost;
import com.coal.erp.business.mapper.maintenance.MaintenanceCostMapper;
import com.coal.erp.business.service.maintenance.IMaintenanceCostService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 维修成本服务实现
 */
@Service
public class MaintenanceCostServiceImpl extends ServiceImpl<MaintenanceCostMapper, MaintenanceCost> 
        implements IMaintenanceCostService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createCost(MaintenanceCost cost) {
        try {
            if (cost.getAmount() == null) {
                cost.setAmount(cost.getQuantity().multiply(cost.getUnitPrice()));
            }
            cost.setCreateTime(new Date());
            cost.setUpdateTime(new Date());
            save(cost);
            return R.success(cost);
        } catch (Exception e) {
            return R.error("创建成本记录失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenanceCost> pageCost(Long current, Long size, Long workOrderId, String costType) {
        Page<MaintenanceCost> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenanceCost> wrapper = new LambdaQueryWrapper<>();
        if (workOrderId != null) {
            wrapper.eq(MaintenanceCost::getWorkOrderId, workOrderId);
        }
        if (costType != null && !costType.isEmpty()) {
            wrapper.eq(MaintenanceCost::getCostType, costType);
        }
        wrapper.orderByDesc(MaintenanceCost::getCostDate);
        return page(page, wrapper);
    }
}

