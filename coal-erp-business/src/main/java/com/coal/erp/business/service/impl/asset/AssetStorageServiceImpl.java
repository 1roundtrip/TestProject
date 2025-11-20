package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.asset.AssetStorage;
import com.coal.erp.business.domain.asset.AssetStorageDetail;
import com.coal.erp.business.mapper.AssetMapper;
import com.coal.erp.business.mapper.asset.AssetStorageDetailMapper;
import com.coal.erp.business.mapper.asset.AssetStorageMapper;
import com.coal.erp.business.service.asset.IAssetStorageService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 资产入库服务实现
 */
@Service
public class AssetStorageServiceImpl extends ServiceImpl<AssetStorageMapper, AssetStorage> 
        implements IAssetStorageService {
    
    @Autowired
    private AssetStorageDetailMapper storageDetailMapper;
    
    @Autowired
    private AssetMapper assetMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createStorage(AssetStorage storage, List<AssetStorageDetail> details) {
        // 生成入库单号
        storage.setStorageNo("RK" + System.currentTimeMillis());
        storage.setStatus("DRAFT");
        storage.setCreateUserId(SecurityUtils.getUserId());
        storage.setCreateUserName(SecurityUtils.getUsername());
        storage.setCreateTime(new Date());
        
        // 计算总金额
        BigDecimal totalAmount = details.stream()
            .map(detail -> {
                if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                    detail.setTotalPrice(detail.getUnitPrice()
                        .multiply(new BigDecimal(detail.getQuantity())));
                    return detail.getTotalPrice();
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        storage.setTotalAmount(totalAmount);
        
        // 保存入库单
        save(storage);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setStorageId(storage.getStorageId());
            detail.setCreateTime(new Date());
            storageDetailMapper.insert(detail);
        });
        
        return R.success(storage);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmStorage(Long storageId) {
        AssetStorage storage = getById(storageId);
        if (storage == null) {
            return R.error("入库单不存在");
        }
        if (!"DRAFT".equals(storage.getStatus())) {
            return R.error("只能确认草稿状态的入库单");
        }
        
        // 更新状态
        storage.setStatus("CONFIRMED");
        storage.setAuditUserId(SecurityUtils.getUserId());
        storage.setAuditUserName(SecurityUtils.getUsername());
        storage.setAuditTime(new Date());
        updateById(storage);
        
        // 创建或更新资产
        List<AssetStorageDetail> details = storageDetailMapper.selectList(
            new LambdaQueryWrapper<AssetStorageDetail>()
                .eq(AssetStorageDetail::getStorageId, storageId)
        );
        
        for (AssetStorageDetail detail : details) {
            if (detail.getAssetId() == null) {
                // 创建新资产
                Asset asset = new Asset();
                asset.setAssetCode(detail.getAssetCode());
                asset.setAssetName(detail.getAssetName());
                asset.setAssetType(detail.getAssetType());
                asset.setCategory(detail.getCategory());
                asset.setManufacturer(detail.getManufacturer());
                asset.setModel(detail.getModel());
                asset.setSerialNumber(detail.getSerialNumber());
                asset.setPurchaseDate(detail.getPurchaseDate());
                if (detail.getUnitPrice() != null) {
                    asset.setPurchasePrice(detail.getUnitPrice());
                }
                asset.setStatus("0");
                asset.setLocation(storage.getLocation());
                asset.setCreateTime(new Date());
                assetMapper.insert(asset);
                
                // 更新明细中的资产ID
                detail.setAssetId(asset.getAssetId());
                storageDetailMapper.updateById(detail);
            } else {
                // 更新现有资产
                Asset asset = assetMapper.selectById(detail.getAssetId());
                if (asset != null) {
                    asset.setUpdateTime(new Date());
                    assetMapper.updateById(asset);
                }
            }
        }
        
        return R.success("确认成功");
    }
    
    @Override
    public R<?> cancelStorage(Long storageId) {
        AssetStorage storage = getById(storageId);
        if (storage == null) {
            return R.error("入库单不存在");
        }
        if ("CONFIRMED".equals(storage.getStatus())) {
            return R.error("已确认的入库单不能取消");
        }
        storage.setStatus("CANCELLED");
        updateById(storage);
        return R.success("取消成功");
    }
    
    @Override
    public Page<AssetStorage> pageStorage(Long current, Long size, String storageNo, String status) {
        Page<AssetStorage> page = new Page<>(current, size);
        LambdaQueryWrapper<AssetStorage> wrapper = new LambdaQueryWrapper<>();
        if (storageNo != null && !storageNo.isEmpty()) {
            wrapper.like(AssetStorage::getStorageNo, storageNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(AssetStorage::getStatus, status);
        }
        wrapper.orderByDesc(AssetStorage::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<AssetStorageDetail> getStorageDetails(Long storageId) {
        return storageDetailMapper.selectList(
            new LambdaQueryWrapper<AssetStorageDetail>()
                .eq(AssetStorageDetail::getStorageId, storageId)
        );
    }
}

