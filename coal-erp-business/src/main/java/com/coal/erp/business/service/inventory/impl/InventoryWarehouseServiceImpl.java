package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryWarehouse;
import com.coal.erp.business.mapper.inventory.InventoryWarehouseMapper;
import com.coal.erp.business.service.inventory.IInventoryWarehouseService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 仓库服务实现
 */
@Service
public class InventoryWarehouseServiceImpl extends ServiceImpl<InventoryWarehouseMapper, InventoryWarehouse>
        implements IInventoryWarehouseService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createWarehouse(InventoryWarehouse warehouse) {
        try {
            if (!checkWarehouseCodeUnique(warehouse)) {
                return R.error("仓库编码已存在");
            }
            
            warehouse.setStatus("ACTIVE");
            warehouse.setCreateTime(new Date());
            warehouse.setUpdateTime(new Date());
            
            save(warehouse);
            return R.success(warehouse);
        } catch (Exception e) {
            return R.error("创建仓库失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateWarehouse(InventoryWarehouse warehouse) {
        try {
            if (!checkWarehouseCodeUnique(warehouse)) {
                return R.error("仓库编码已存在");
            }
            
            warehouse.setUpdateTime(new Date());
            updateById(warehouse);
            return R.success();
        } catch (Exception e) {
            return R.error("更新仓库失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryWarehouse> pageWarehouse(Long current, Long size, String warehouseCode, String warehouseName, String status) {
        Page<InventoryWarehouse> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryWarehouse> wrapper = new LambdaQueryWrapper<>();
        
        if (warehouseCode != null && !warehouseCode.isEmpty()) {
            wrapper.like(InventoryWarehouse::getWarehouseCode, warehouseCode);
        }
        if (warehouseName != null && !warehouseName.isEmpty()) {
            wrapper.like(InventoryWarehouse::getWarehouseName, warehouseName);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(InventoryWarehouse::getStatus, status);
        }
        
        wrapper.orderByDesc(InventoryWarehouse::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public boolean checkWarehouseCodeUnique(InventoryWarehouse warehouse) {
        LambdaQueryWrapper<InventoryWarehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryWarehouse::getWarehouseCode, warehouse.getWarehouseCode());
        if (warehouse.getWarehouseId() != null) {
            wrapper.ne(InventoryWarehouse::getWarehouseId, warehouse.getWarehouseId());
        }
        return count(wrapper) == 0;
    }
}

