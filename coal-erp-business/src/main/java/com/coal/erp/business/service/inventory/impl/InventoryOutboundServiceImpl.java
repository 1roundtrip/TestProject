package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryOutbound;
import com.coal.erp.business.domain.inventory.InventoryOutboundDetail;
import com.coal.erp.business.mapper.inventory.InventoryOutboundDetailMapper;
import com.coal.erp.business.mapper.inventory.InventoryOutboundMapper;
import com.coal.erp.business.service.inventory.IInventoryOutboundService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 出库服务实现
 */
@Service
public class InventoryOutboundServiceImpl extends ServiceImpl<InventoryOutboundMapper, InventoryOutbound>
        implements IInventoryOutboundService {
    
    @Autowired
    private InventoryOutboundDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createOutbound(InventoryOutbound outbound, List<InventoryOutboundDetail> details) {
        try {
            if (outbound.getOutboundNo() == null || outbound.getOutboundNo().isEmpty()) {
                outbound.setOutboundNo("OUT" + System.currentTimeMillis());
            }
            
            outbound.setStatus("DRAFT");
            outbound.setCreateTime(new Date());
            outbound.setUpdateTime(new Date());
            
            save(outbound);
            
            if (details != null && !details.isEmpty()) {
                for (InventoryOutboundDetail detail : details) {
                    detail.setOutboundId(outbound.getOutboundId());
                    detailMapper.insert(detail);
                }
            }
            
            return R.success(outbound);
        } catch (Exception e) {
            return R.error("创建出库单失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateOutbound(InventoryOutbound outbound, List<InventoryOutboundDetail> details) {
        try {
            outbound.setUpdateTime(new Date());
            updateById(outbound);
            
            // 删除原有明细
            LambdaQueryWrapper<InventoryOutboundDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(InventoryOutboundDetail::getOutboundId, outbound.getOutboundId());
            detailMapper.delete(wrapper);
            
            // 插入新明细
            if (details != null && !details.isEmpty()) {
                for (InventoryOutboundDetail detail : details) {
                    detail.setOutboundId(outbound.getOutboundId());
                    detailMapper.insert(detail);
                }
            }
            
            return R.success();
        } catch (Exception e) {
            return R.error("更新出库单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryOutbound> pageOutbound(Long current, Long size, String outboundNo, String outboundType, String status, Long warehouseId) {
        Page<InventoryOutbound> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryOutbound> wrapper = new LambdaQueryWrapper<>();
        
        if (outboundNo != null && !outboundNo.isEmpty()) {
            wrapper.like(InventoryOutbound::getOutboundNo, outboundNo);
        }
        if (outboundType != null && !outboundType.isEmpty()) {
            wrapper.eq(InventoryOutbound::getOutboundType, outboundType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(InventoryOutbound::getStatus, status);
        }
        if (warehouseId != null) {
            wrapper.eq(InventoryOutbound::getWarehouseId, warehouseId);
        }
        
        wrapper.orderByDesc(InventoryOutbound::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitOutbound(Long outboundId) {
        try {
            InventoryOutbound outbound = getById(outboundId);
            if (outbound == null) {
                return R.error("出库单不存在");
            }
            outbound.setStatus("SUBMITTED");
            outbound.setUpdateTime(new Date());
            updateById(outbound);
            return R.success();
        } catch (Exception e) {
            return R.error("提交失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveOutbound(Long outboundId) {
        try {
            InventoryOutbound outbound = getById(outboundId);
            if (outbound == null) {
                return R.error("出库单不存在");
            }
            outbound.setStatus("APPROVED");
            outbound.setApproveTime(new Date());
            outbound.setUpdateTime(new Date());
            updateById(outbound);
            return R.success();
        } catch (Exception e) {
            return R.error("审批失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> issueOutbound(Long outboundId) {
        try {
            InventoryOutbound outbound = getById(outboundId);
            if (outbound == null) {
                return R.error("出库单不存在");
            }
            outbound.setStatus("ISSUED");
            outbound.setIssueTime(new Date());
            outbound.setUpdateTime(new Date());
            updateById(outbound);
            // TODO: 更新库存
            return R.success();
        } catch (Exception e) {
            return R.error("发放失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<InventoryOutboundDetail> getOutboundDetails(Long outboundId) {
        LambdaQueryWrapper<InventoryOutboundDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryOutboundDetail::getOutboundId, outboundId);
        return detailMapper.selectList(wrapper);
    }
}

