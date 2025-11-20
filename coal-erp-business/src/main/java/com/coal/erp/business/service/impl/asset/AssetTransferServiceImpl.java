package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.asset.AssetTransfer;
import com.coal.erp.business.mapper.AssetMapper;
import com.coal.erp.business.mapper.asset.AssetTransferMapper;
import com.coal.erp.business.service.asset.IAssetTransferService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 资产转移调拨服务实现
 */
@Service
public class AssetTransferServiceImpl extends ServiceImpl<AssetTransferMapper, AssetTransfer> 
        implements IAssetTransferService {
    
    @Autowired
    private AssetMapper assetMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createTransfer(AssetTransfer transfer) {
        // 生成转移单号
        transfer.setTransferNo("ZY" + System.currentTimeMillis());
        transfer.setStatus("PENDING");
        transfer.setCreateUserId(SecurityUtils.getUserId());
        transfer.setCreateUserName(SecurityUtils.getUsername());
        transfer.setCreateTime(new Date());
        
        // 检查资产是否存在
        Asset asset = assetMapper.selectById(transfer.getAssetId());
        if (asset == null) {
            return R.error("资产不存在");
        }
        
        // 设置原部门和位置
        if (transfer.getFromDeptId() == null) {
            transfer.setFromDeptId(asset.getDeptId());
        }
        if (transfer.getFromLocation() == null) {
            transfer.setFromLocation(asset.getLocation());
        }
        
        save(transfer);
        return R.success(transfer);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveTransfer(Long transferId, String approveRemark) {
        AssetTransfer transfer = getById(transferId);
        if (transfer == null) {
            return R.error("转移单不存在");
        }
        if (!"PENDING".equals(transfer.getStatus())) {
            return R.error("只能审批待转移状态的转移单");
        }
        
        transfer.setStatus("APPROVED");
        transfer.setApproveUserId(SecurityUtils.getUserId());
        transfer.setApproveUserName(SecurityUtils.getUsername());
        transfer.setApproveTime(new Date());
        transfer.setRemark(approveRemark);
        updateById(transfer);
        
        return R.success("审批通过");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> rejectTransfer(Long transferId, String rejectRemark) {
        AssetTransfer transfer = getById(transferId);
        if (transfer == null) {
            return R.error("转移单不存在");
        }
        if (!"PENDING".equals(transfer.getStatus())) {
            return R.error("只能驳回待转移状态的转移单");
        }
        
        transfer.setStatus("REJECTED");
        transfer.setApproveUserId(SecurityUtils.getUserId());
        transfer.setApproveUserName(SecurityUtils.getUsername());
        transfer.setApproveTime(new Date());
        transfer.setRemark(rejectRemark);
        updateById(transfer);
        
        return R.success("已驳回");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> executeTransfer(Long transferId) {
        AssetTransfer transfer = getById(transferId);
        if (transfer == null) {
            return R.error("转移单不存在");
        }
        if (!"APPROVED".equals(transfer.getStatus())) {
            return R.error("只能执行已审批的转移单");
        }
        
        // 更新资产部门和位置
        Asset asset = assetMapper.selectById(transfer.getAssetId());
        if (asset == null) {
            return R.error("资产不存在");
        }
        
        asset.setDeptId(transfer.getToDeptId());
        asset.setLocation(transfer.getToLocation());
        asset.setUpdateTime(new Date());
        assetMapper.updateById(asset);
        
        // 更新转移单状态
        transfer.setStatus("TRANSFERRED");
        transfer.setTransferUserId(SecurityUtils.getUserId());
        transfer.setTransferUserName(SecurityUtils.getUsername());
        transfer.setTransferTime(new Date());
        updateById(transfer);
        
        return R.success("转移成功");
    }
    
    @Override
    public Page<AssetTransfer> pageTransfer(Long current, Long size, String transferNo, String status) {
        Page<AssetTransfer> page = new Page<>(current, size);
        LambdaQueryWrapper<AssetTransfer> wrapper = new LambdaQueryWrapper<>();
        if (transferNo != null && !transferNo.isEmpty()) {
            wrapper.like(AssetTransfer::getTransferNo, transferNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(AssetTransfer::getStatus, status);
        }
        wrapper.orderByDesc(AssetTransfer::getCreateTime);
        return page(page, wrapper);
    }
}

