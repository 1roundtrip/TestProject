package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryStock;
import com.coal.erp.business.mapper.inventory.InventoryStockMapper;
import com.coal.erp.business.service.inventory.IInventoryStockService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存明细服务实现
 */
@Service
public class InventoryStockServiceImpl extends ServiceImpl<InventoryStockMapper, InventoryStock>
        implements IInventoryStockService {
    
    @Override
    public Page<InventoryStock> pageStock(Long current, Long size, Long warehouseId, Long locationId, String materialCode, String materialName) {
        Page<InventoryStock> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
        
        if (warehouseId != null) {
            wrapper.eq(InventoryStock::getWarehouseId, warehouseId);
        }
        if (locationId != null) {
            wrapper.eq(InventoryStock::getLocationId, locationId);
        }
        if (materialCode != null && !materialCode.isEmpty()) {
            wrapper.like(InventoryStock::getMaterialCode, materialCode);
        }
        if (materialName != null && !materialName.isEmpty()) {
            wrapper.like(InventoryStock::getMaterialName, materialName);
        }
        
        wrapper.orderByDesc(InventoryStock::getUpdateTime);
        return page(page, wrapper);
    }
    
    @Override
    public R<?> getMaterialStockSummary(Long materialId) {
        try {
            LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InventoryStock::getMaterialId, materialId);
            
            java.util.List<InventoryStock> stocks = list(wrapper);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalQuantity", stocks.stream()
                .map(InventoryStock::getQuantity)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
            summary.put("totalValue", stocks.stream()
                .map(InventoryStock::getTotalValue)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
            summary.put("warehouseCount", stocks.stream()
                .map(InventoryStock::getWarehouseId)
                .distinct()
                .count());
            
            return R.success(summary);
        } catch (Exception e) {
            return R.error("获取库存汇总失败：" + e.getMessage());
        }
    }
    
    @Override
    public R<?> getWarehouseStockSummary(Long warehouseId) {
        try {
            LambdaQueryWrapper<InventoryStock> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InventoryStock::getWarehouseId, warehouseId);
            
            java.util.List<InventoryStock> stocks = list(wrapper);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalMaterials", stocks.stream()
                .map(InventoryStock::getMaterialId)
                .distinct()
                .count());
            summary.put("totalQuantity", stocks.stream()
                .map(InventoryStock::getQuantity)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
            summary.put("totalValue", stocks.stream()
                .map(InventoryStock::getTotalValue)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
            
            return R.success(summary);
        } catch (Exception e) {
            return R.error("获取仓库汇总失败：" + e.getMessage());
        }
    }
}

