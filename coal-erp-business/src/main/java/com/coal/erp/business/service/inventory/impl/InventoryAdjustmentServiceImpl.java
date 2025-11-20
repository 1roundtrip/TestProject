package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryAdjustment;
import com.coal.erp.business.domain.inventory.InventoryAdjustmentDetail;
import com.coal.erp.business.mapper.inventory.InventoryAdjustmentDetailMapper;
import com.coal.erp.business.mapper.inventory.InventoryAdjustmentMapper;
import com.coal.erp.business.service.inventory.IInventoryAdjustmentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 库存调整服务实现
 */
@Service
public class InventoryAdjustmentServiceImpl extends ServiceImpl<InventoryAdjustmentMapper, InventoryAdjustment>
        implements IInventoryAdjustmentService {
    
    @Autowired
    private InventoryAdjustmentDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createAdjustment(InventoryAdjustment adjustment, List<InventoryAdjustmentDetail> details) {
        try {
            if (adjustment.getAdjustmentNo() == null || adjustment.getAdjustmentNo().isEmpty()) {
                adjustment.setAdjustmentNo("ADJ" + System.currentTimeMillis());
            }
            adjustment.setStatus("DRAFT");
            adjustment.setCreateTime(new Date());
            adjustment.setUpdateTime(new Date());
            save(adjustment);
            if (details != null && !details.isEmpty()) {
                for (InventoryAdjustmentDetail detail : details) {
                    detail.setAdjustmentId(adjustment.getAdjustmentId());
                    detailMapper.insert(detail);
                }
            }
            return R.success(adjustment);
        } catch (Exception e) {
            return R.error("创建调整单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryAdjustment> pageAdjustment(Long current, Long size, String adjustmentNo, String adjustmentType, String status, Long warehouseId) {
        Page<InventoryAdjustment> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryAdjustment> wrapper = new LambdaQueryWrapper<>();
        if (adjustmentNo != null && !adjustmentNo.isEmpty()) wrapper.like(InventoryAdjustment::getAdjustmentNo, adjustmentNo);
        if (adjustmentType != null && !adjustmentType.isEmpty()) wrapper.eq(InventoryAdjustment::getAdjustmentType, adjustmentType);
        if (status != null && !status.isEmpty()) wrapper.eq(InventoryAdjustment::getStatus, status);
        if (warehouseId != null) wrapper.eq(InventoryAdjustment::getWarehouseId, warehouseId);
        wrapper.orderByDesc(InventoryAdjustment::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitAdjustment(Long adjustmentId) {
        InventoryAdjustment adjustment = getById(adjustmentId);
        if (adjustment == null) return R.error("调整单不存在");
        adjustment.setStatus("SUBMITTED");
        adjustment.setUpdateTime(new Date());
        updateById(adjustment);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveAdjustment(Long adjustmentId) {
        InventoryAdjustment adjustment = getById(adjustmentId);
        if (adjustment == null) return R.error("调整单不存在");
        adjustment.setStatus("COMPLETED");
        adjustment.setApproveTime(new Date());
        adjustment.setUpdateTime(new Date());
        updateById(adjustment);
        // TODO: 更新库存
        return R.success();
    }
    
    @Override
    public List<InventoryAdjustmentDetail> getAdjustmentDetails(Long adjustmentId) {
        LambdaQueryWrapper<InventoryAdjustmentDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryAdjustmentDetail::getAdjustmentId, adjustmentId);
        return detailMapper.selectList(wrapper);
    }
}

