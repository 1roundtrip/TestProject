package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryLocation;
import com.coal.erp.business.mapper.inventory.InventoryLocationMapper;
import com.coal.erp.business.service.inventory.IInventoryLocationService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 库位服务实现
 */
@Service
public class InventoryLocationServiceImpl extends ServiceImpl<InventoryLocationMapper, InventoryLocation>
        implements IInventoryLocationService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createLocation(InventoryLocation location) {
        try {
            if (!checkLocationCodeUnique(location)) {
                return R.error("库位编码已存在");
            }
            
            location.setStatus("ACTIVE");
            location.setCreateTime(new Date());
            location.setUpdateTime(new Date());
            
            save(location);
            return R.success(location);
        } catch (Exception e) {
            return R.error("创建库位失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateLocation(InventoryLocation location) {
        try {
            if (!checkLocationCodeUnique(location)) {
                return R.error("库位编码已存在");
            }
            
            location.setUpdateTime(new Date());
            updateById(location);
            return R.success();
        } catch (Exception e) {
            return R.error("更新库位失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryLocation> pageLocation(Long current, Long size, Long warehouseId, String locationCode, String status) {
        Page<InventoryLocation> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryLocation> wrapper = new LambdaQueryWrapper<>();
        
        if (warehouseId != null) {
            wrapper.eq(InventoryLocation::getWarehouseId, warehouseId);
        }
        if (locationCode != null && !locationCode.isEmpty()) {
            wrapper.like(InventoryLocation::getLocationCode, locationCode);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(InventoryLocation::getStatus, status);
        }
        
        wrapper.orderByDesc(InventoryLocation::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public boolean checkLocationCodeUnique(InventoryLocation location) {
        LambdaQueryWrapper<InventoryLocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryLocation::getWarehouseId, location.getWarehouseId());
        wrapper.eq(InventoryLocation::getLocationCode, location.getLocationCode());
        if (location.getLocationId() != null) {
            wrapper.ne(InventoryLocation::getLocationId, location.getLocationId());
        }
        return count(wrapper) == 0;
    }
}

