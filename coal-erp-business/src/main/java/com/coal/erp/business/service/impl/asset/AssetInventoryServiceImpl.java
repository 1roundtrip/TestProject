package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.asset.AssetInventory;
import com.coal.erp.business.domain.asset.AssetInventoryDetail;
import com.coal.erp.business.mapper.AssetMapper;
import com.coal.erp.business.mapper.asset.AssetInventoryDetailMapper;
import com.coal.erp.business.mapper.asset.AssetInventoryMapper;
import com.coal.erp.business.service.asset.IAssetInventoryService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 资产盘点服务实现
 */
@Service
public class AssetInventoryServiceImpl extends ServiceImpl<AssetInventoryMapper, AssetInventory> 
        implements IAssetInventoryService {
    
    @Autowired
    private AssetInventoryDetailMapper detailMapper;
    
    @Autowired
    private AssetMapper assetMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createInventory(AssetInventory inventory) {
        // 生成盘点单号
        inventory.setInventoryNo("PD" + System.currentTimeMillis());
        inventory.setStatus("DRAFT");
        inventory.setCreateUserId(SecurityUtils.getUserId());
        inventory.setCreateUserName(SecurityUtils.getUsername());
        inventory.setCreateTime(new Date());
        inventory.setTotalCount(0);
        inventory.setActualCount(0);
        inventory.setSurplusCount(0);
        inventory.setShortageCount(0);
        save(inventory);
        return R.success(inventory);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> addInventoryDetail(Long inventoryId, List<AssetInventoryDetail> details) {
        AssetInventory inventory = getById(inventoryId);
        if (inventory == null) {
            return R.error("盘点单不存在");
        }
        
        for (AssetInventoryDetail detail : details) {
            detail.setInventoryId(inventoryId);
            detail.setCreateTime(new Date());
            
            // 获取资产信息
            Asset asset = assetMapper.selectById(detail.getAssetId());
            if (asset != null) {
                detail.setAssetCode(asset.getAssetCode());
                detail.setAssetName(asset.getAssetName());
                detail.setBookQuantity(1); // 默认账面数量为1
            }
            
            detailMapper.insert(detail);
        }
        
        // 更新盘点单数量
        inventory.setTotalCount(inventory.getTotalCount() + details.size());
        updateById(inventory);
        
        return R.success("添加成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> startInventory(Long inventoryId) {
        AssetInventory inventory = getById(inventoryId);
        if (inventory == null) {
            return R.error("盘点单不存在");
        }
        if (!"DRAFT".equals(inventory.getStatus())) {
            return R.error("只能开始草稿状态的盘点单");
        }
        inventory.setStatus("IN_PROGRESS");
        inventory.setInventoryUserId(SecurityUtils.getUserId());
        inventory.setInventoryUserName(SecurityUtils.getUsername());
        updateById(inventory);
        return R.success("开始盘点");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> completeInventory(Long inventoryId) {
        AssetInventory inventory = getById(inventoryId);
        if (inventory == null) {
            return R.error("盘点单不存在");
        }
        if (!"IN_PROGRESS".equals(inventory.getStatus())) {
            return R.error("只能完成盘点中的盘点单");
        }
        
        // 统计盘点结果
        List<AssetInventoryDetail> details = detailMapper.selectList(
            new LambdaQueryWrapper<AssetInventoryDetail>()
                .eq(AssetInventoryDetail::getInventoryId, inventoryId)
        );
        
        int actualCount = 0;
        int surplusCount = 0;
        int shortageCount = 0;
        
        for (AssetInventoryDetail detail : details) {
            if (detail.getActualQuantity() != null) {
                actualCount += detail.getActualQuantity();
                int difference = detail.getActualQuantity() - (detail.getBookQuantity() != null ? detail.getBookQuantity() : 0);
                detail.setDifferenceQuantity(difference);
                
                if (difference > 0) {
                    detail.setDifferenceType("SURPLUS");
                    surplusCount++;
                } else if (difference < 0) {
                    detail.setDifferenceType("SHORTAGE");
                    shortageCount++;
                } else {
                    detail.setDifferenceType("NORMAL");
                }
                detailMapper.updateById(detail);
            }
        }
        
        inventory.setActualCount(actualCount);
        inventory.setSurplusCount(surplusCount);
        inventory.setShortageCount(shortageCount);
        inventory.setStatus("COMPLETED");
        updateById(inventory);
        
        return R.success("盘点完成");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmInventory(Long inventoryId) {
        AssetInventory inventory = getById(inventoryId);
        if (inventory == null) {
            return R.error("盘点单不存在");
        }
        if (!"COMPLETED".equals(inventory.getStatus())) {
            return R.error("只能确认已完成的盘点单");
        }
        
        inventory.setStatus("CONFIRMED");
        inventory.setConfirmUserId(SecurityUtils.getUserId());
        inventory.setConfirmUserName(SecurityUtils.getUsername());
        inventory.setConfirmTime(new Date());
        updateById(inventory);
        
        // 处理盘盈盘亏，更新资产状态
        List<AssetInventoryDetail> details = detailMapper.selectList(
            new LambdaQueryWrapper<AssetInventoryDetail>()
                .eq(AssetInventoryDetail::getInventoryId, inventoryId)
                .in(AssetInventoryDetail::getDifferenceType, "SURPLUS", "SHORTAGE")
        );
        
        for (AssetInventoryDetail detail : details) {
            Asset asset = assetMapper.selectById(detail.getAssetId());
            if (asset != null) {
                // 可以根据业务需求更新资产状态或位置
                asset.setUpdateTime(new Date());
                assetMapper.updateById(asset);
            }
        }
        
        return R.success("确认成功");
    }
    
    @Override
    public R<?> handleDifference(Long detailId, String handleRemark) {
        AssetInventoryDetail detail = detailMapper.selectById(detailId);
        if (detail == null) {
            return R.error("盘点明细不存在");
        }
        detail.setHandleStatus("PROCESSED");
        detail.setHandleRemark(handleRemark);
        detailMapper.updateById(detail);
        return R.success("处理成功");
    }
    
    @Override
    public Page<AssetInventory> pageInventory(Long current, Long size, String inventoryNo, String status) {
        Page<AssetInventory> page = new Page<>(current, size);
        LambdaQueryWrapper<AssetInventory> wrapper = new LambdaQueryWrapper<>();
        if (inventoryNo != null && !inventoryNo.isEmpty()) {
            wrapper.like(AssetInventory::getInventoryNo, inventoryNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(AssetInventory::getStatus, status);
        }
        wrapper.orderByDesc(AssetInventory::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<AssetInventoryDetail> getInventoryDetails(Long inventoryId) {
        return detailMapper.selectList(
            new LambdaQueryWrapper<AssetInventoryDetail>()
                .eq(AssetInventoryDetail::getInventoryId, inventoryId)
        );
    }
}

