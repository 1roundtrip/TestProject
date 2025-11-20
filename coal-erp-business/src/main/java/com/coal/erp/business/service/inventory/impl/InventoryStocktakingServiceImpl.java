package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryStocktaking;
import com.coal.erp.business.domain.inventory.InventoryStocktakingDetail;
import com.coal.erp.business.mapper.inventory.InventoryStocktakingDetailMapper;
import com.coal.erp.business.mapper.inventory.InventoryStocktakingMapper;
import com.coal.erp.business.service.inventory.IInventoryStocktakingService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 库存盘点服务实现
 */
@Service
public class InventoryStocktakingServiceImpl extends ServiceImpl<InventoryStocktakingMapper, InventoryStocktaking>
        implements IInventoryStocktakingService {
    
    @Autowired
    private InventoryStocktakingDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createStocktaking(InventoryStocktaking stocktaking, List<InventoryStocktakingDetail> details) {
        try {
            if (stocktaking.getStocktakingNo() == null || stocktaking.getStocktakingNo().isEmpty()) {
                stocktaking.setStocktakingNo("ST" + System.currentTimeMillis());
            }
            stocktaking.setStatus("DRAFT");
            stocktaking.setCreateTime(new Date());
            stocktaking.setUpdateTime(new Date());
            save(stocktaking);
            if (details != null && !details.isEmpty()) {
                for (InventoryStocktakingDetail detail : details) {
                    detail.setStocktakingId(stocktaking.getStocktakingId());
                    detailMapper.insert(detail);
                }
            }
            return R.success(stocktaking);
        } catch (Exception e) {
            return R.error("创建盘点单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryStocktaking> pageStocktaking(Long current, Long size, String stocktakingNo, String status, Long warehouseId) {
        Page<InventoryStocktaking> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryStocktaking> wrapper = new LambdaQueryWrapper<>();
        if (stocktakingNo != null && !stocktakingNo.isEmpty()) wrapper.like(InventoryStocktaking::getStocktakingNo, stocktakingNo);
        if (status != null && !status.isEmpty()) wrapper.eq(InventoryStocktaking::getStatus, status);
        if (warehouseId != null) wrapper.eq(InventoryStocktaking::getWarehouseId, warehouseId);
        wrapper.orderByDesc(InventoryStocktaking::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> startStocktaking(Long stocktakingId) {
        InventoryStocktaking stocktaking = getById(stocktakingId);
        if (stocktaking == null) return R.error("盘点单不存在");
        stocktaking.setStatus("IN_PROGRESS");
        stocktaking.setStartTime(new Date());
        stocktaking.setUpdateTime(new Date());
        updateById(stocktaking);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> completeStocktaking(Long stocktakingId) {
        InventoryStocktaking stocktaking = getById(stocktakingId);
        if (stocktaking == null) return R.error("盘点单不存在");
        stocktaking.setStatus("COMPLETED");
        stocktaking.setEndTime(new Date());
        stocktaking.setUpdateTime(new Date());
        updateById(stocktaking);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmStocktaking(Long stocktakingId) {
        InventoryStocktaking stocktaking = getById(stocktakingId);
        if (stocktaking == null) return R.error("盘点单不存在");
        stocktaking.setStatus("CONFIRMED");
        stocktaking.setConfirmTime(new Date());
        stocktaking.setUpdateTime(new Date());
        updateById(stocktaking);
        // TODO: 生成调整单
        return R.success();
    }
    
    @Override
    public List<InventoryStocktakingDetail> getStocktakingDetails(Long stocktakingId) {
        LambdaQueryWrapper<InventoryStocktakingDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryStocktakingDetail::getStocktakingId, stocktakingId);
        return detailMapper.selectList(wrapper);
    }
}

