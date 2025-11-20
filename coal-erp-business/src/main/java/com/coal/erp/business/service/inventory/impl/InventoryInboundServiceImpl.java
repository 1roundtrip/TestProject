package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryInbound;
import com.coal.erp.business.domain.inventory.InventoryInboundDetail;
import com.coal.erp.business.mapper.inventory.InventoryInboundDetailMapper;
import com.coal.erp.business.mapper.inventory.InventoryInboundMapper;
import com.coal.erp.business.service.inventory.IInventoryInboundService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 入库服务实现
 */
@Service
public class InventoryInboundServiceImpl extends ServiceImpl<InventoryInboundMapper, InventoryInbound>
        implements IInventoryInboundService {
    
    @Autowired
    private InventoryInboundDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createInbound(InventoryInbound inbound, List<InventoryInboundDetail> details) {
        try {
            if (inbound.getInboundNo() == null || inbound.getInboundNo().isEmpty()) {
                inbound.setInboundNo("IN" + System.currentTimeMillis());
            }
            
            inbound.setStatus("DRAFT");
            inbound.setCreateTime(new Date());
            inbound.setUpdateTime(new Date());
            
            save(inbound);
            
            if (details != null && !details.isEmpty()) {
                for (InventoryInboundDetail detail : details) {
                    detail.setInboundId(inbound.getInboundId());
                    detailMapper.insert(detail);
                }
            }
            
            return R.success(inbound);
        } catch (Exception e) {
            return R.error("创建入库单失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateInbound(InventoryInbound inbound, List<InventoryInboundDetail> details) {
        try {
            inbound.setUpdateTime(new Date());
            updateById(inbound);
            
            // 删除原有明细
            LambdaQueryWrapper<InventoryInboundDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InventoryInboundDetail::getInboundId, inbound.getInboundId());
            detailMapper.delete(wrapper);
            
            // 插入新明细
            if (details != null && !details.isEmpty()) {
                for (InventoryInboundDetail detail : details) {
                    detail.setInboundId(inbound.getInboundId());
                    detailMapper.insert(detail);
                }
            }
            
            return R.success();
        } catch (Exception e) {
            return R.error("更新入库单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryInbound> pageInbound(Long current, Long size, String inboundNo, String inboundType, String status, Long warehouseId) {
        Page<InventoryInbound> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryInbound> wrapper = new LambdaQueryWrapper<>();
        
        if (inboundNo != null && !inboundNo.isEmpty()) {
            wrapper.like(InventoryInbound::getInboundNo, inboundNo);
        }
        if (inboundType != null && !inboundType.isEmpty()) {
            wrapper.eq(InventoryInbound::getInboundType, inboundType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(InventoryInbound::getStatus, status);
        }
        if (warehouseId != null) {
            wrapper.eq(InventoryInbound::getWarehouseId, warehouseId);
        }
        
        wrapper.orderByDesc(InventoryInbound::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitInbound(Long inboundId) {
        try {
            InventoryInbound inbound = getById(inboundId);
            if (inbound == null) {
                return R.error("入库单不存在");
            }
            inbound.setStatus("SUBMITTED");
            inbound.setUpdateTime(new Date());
            updateById(inbound);
            return R.success();
        } catch (Exception e) {
            return R.error("提交失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveInbound(Long inboundId) {
        try {
            InventoryInbound inbound = getById(inboundId);
            if (inbound == null) {
                return R.error("入库单不存在");
            }
            inbound.setStatus("APPROVED");
            inbound.setApproveTime(new Date());
            inbound.setUpdateTime(new Date());
            updateById(inbound);
            return R.success();
        } catch (Exception e) {
            return R.error("审批失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> receiveInbound(Long inboundId) {
        try {
            InventoryInbound inbound = getById(inboundId);
            if (inbound == null) {
                return R.error("入库单不存在");
            }
            inbound.setStatus("RECEIVED");
            inbound.setUpdateTime(new Date());
            updateById(inbound);
            // TODO: 更新库存
            return R.success();
        } catch (Exception e) {
            return R.error("收货失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<InventoryInboundDetail> getInboundDetails(Long inboundId) {
        LambdaQueryWrapper<InventoryInboundDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryInboundDetail::getInboundId, inboundId);
        return detailMapper.selectList(wrapper);
    }
}

