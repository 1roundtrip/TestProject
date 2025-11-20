package com.coal.erp.business.service.inventory.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.inventory.InventoryTransfer;
import com.coal.erp.business.domain.inventory.InventoryTransferDetail;
import com.coal.erp.business.mapper.inventory.InventoryTransferDetailMapper;
import com.coal.erp.business.mapper.inventory.InventoryTransferMapper;
import com.coal.erp.business.service.inventory.IInventoryTransferService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 库存调拨服务实现
 */
@Service
public class InventoryTransferServiceImpl extends ServiceImpl<InventoryTransferMapper, InventoryTransfer>
        implements IInventoryTransferService {
    
    @Autowired
    private InventoryTransferDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createTransfer(InventoryTransfer transfer, List<InventoryTransferDetail> details) {
        try {
            if (transfer.getTransferNo() == null || transfer.getTransferNo().isEmpty()) {
                transfer.setTransferNo("TR" + System.currentTimeMillis());
            }
            transfer.setStatus("DRAFT");
            transfer.setCreateTime(new Date());
            transfer.setUpdateTime(new Date());
            save(transfer);
            if (details != null && !details.isEmpty()) {
                for (InventoryTransferDetail detail : details) {
                    detail.setTransferId(transfer.getTransferId());
                    detailMapper.insert(detail);
                }
            }
            return R.success(transfer);
        } catch (Exception e) {
            return R.error("创建调拨单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<InventoryTransfer> pageTransfer(Long current, Long size, String transferNo, String status, Long fromWarehouseId, Long toWarehouseId) {
        Page<InventoryTransfer> page = new Page<>(current, size);
        LambdaQueryWrapper<InventoryTransfer> wrapper = new LambdaQueryWrapper<>();
        if (transferNo != null && !transferNo.isEmpty()) wrapper.like(InventoryTransfer::getTransferNo, transferNo);
        if (status != null && !status.isEmpty()) wrapper.eq(InventoryTransfer::getStatus, status);
        if (fromWarehouseId != null) wrapper.eq(InventoryTransfer::getFromWarehouseId, fromWarehouseId);
        if (toWarehouseId != null) wrapper.eq(InventoryTransfer::getToWarehouseId, toWarehouseId);
        wrapper.orderByDesc(InventoryTransfer::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitTransfer(Long transferId) {
        InventoryTransfer transfer = getById(transferId);
        if (transfer == null) return R.error("调拨单不存在");
        transfer.setStatus("SUBMITTED");
        transfer.setUpdateTime(new Date());
        updateById(transfer);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveTransfer(Long transferId) {
        InventoryTransfer transfer = getById(transferId);
        if (transfer == null) return R.error("调拨单不存在");
        transfer.setStatus("APPROVED");
        transfer.setApproveTime(new Date());
        transfer.setUpdateTime(new Date());
        updateById(transfer);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> outboundTransfer(Long transferId) {
        InventoryTransfer transfer = getById(transferId);
        if (transfer == null) return R.error("调拨单不存在");
        transfer.setStatus("OUTBOUND");
        transfer.setOutboundTime(new Date());
        transfer.setUpdateTime(new Date());
        updateById(transfer);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> inboundTransfer(Long transferId) {
        InventoryTransfer transfer = getById(transferId);
        if (transfer == null) return R.error("调拨单不存在");
        transfer.setStatus("COMPLETED");
        transfer.setInboundTime(new Date());
        transfer.setUpdateTime(new Date());
        updateById(transfer);
        return R.success();
    }
    
    @Override
    public List<InventoryTransferDetail> getTransferDetails(Long transferId) {
        LambdaQueryWrapper<InventoryTransferDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InventoryTransferDetail::getTransferId, transferId);
        return detailMapper.selectList(wrapper);
    }
}

