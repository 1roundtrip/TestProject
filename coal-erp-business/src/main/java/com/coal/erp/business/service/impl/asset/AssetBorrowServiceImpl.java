package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.asset.AssetBorrow;
import com.coal.erp.business.mapper.AssetMapper;
import com.coal.erp.business.mapper.asset.AssetBorrowMapper;
import com.coal.erp.business.service.asset.IAssetBorrowService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 资产领用退库服务实现
 */
@Service
public class AssetBorrowServiceImpl extends ServiceImpl<AssetBorrowMapper, AssetBorrow> 
        implements IAssetBorrowService {
    
    @Autowired
    private AssetMapper assetMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createBorrow(AssetBorrow borrow) {
        // 生成领用单号
        borrow.setBorrowNo("LY" + System.currentTimeMillis());
        borrow.setBorrowType("BORROW");
        borrow.setStatus("BORROWED");
        borrow.setCreateUserId(SecurityUtils.getUserId());
        borrow.setCreateTime(new Date());
        
        // 检查资产状态
        Asset asset = assetMapper.selectById(borrow.getAssetId());
        if (asset == null) {
            return R.error("资产不存在");
        }
        if (!"0".equals(asset.getStatus())) {
            return R.error("资产当前状态不允许领用");
        }
        
        // 保存领用单
        save(borrow);
        
        // 更新资产状态为已领用（可以扩展状态码）
        asset.setStatus("1"); // 1-已领用
        asset.setUpdateTime(new Date());
        assetMapper.updateById(asset);
        
        return R.success(borrow);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> returnAsset(Long borrowId) {
        AssetBorrow borrow = getById(borrowId);
        if (borrow == null) {
            return R.error("领用单不存在");
        }
        if (!"BORROWED".equals(borrow.getStatus())) {
            return R.error("只能退库已领用的资产");
        }
        
        // 更新领用单状态
        borrow.setStatus("RETURNED");
        borrow.setActualReturnDate(new Date());
        borrow.setUpdateTime(new Date());
        updateById(borrow);
        
        // 更新资产状态为正常
        Asset asset = assetMapper.selectById(borrow.getAssetId());
        if (asset != null) {
            asset.setStatus("0"); // 0-正常
            asset.setUpdateTime(new Date());
            assetMapper.updateById(asset);
        }
        
        return R.success("退库成功");
    }
    
    @Override
    public Page<AssetBorrow> pageBorrow(Long current, Long size, String borrowNo, String status, String borrowType) {
        Page<AssetBorrow> page = new Page<>(current, size);
        LambdaQueryWrapper<AssetBorrow> wrapper = new LambdaQueryWrapper<>();
        if (borrowNo != null && !borrowNo.isEmpty()) {
            wrapper.like(AssetBorrow::getBorrowNo, borrowNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(AssetBorrow::getStatus, status);
        }
        if (borrowType != null && !borrowType.isEmpty()) {
            wrapper.eq(AssetBorrow::getBorrowType, borrowType);
        }
        wrapper.orderByDesc(AssetBorrow::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public R<?> getOverdueBorrows() {
        Date now = new Date();
        List<AssetBorrow> overdueList = list(new LambdaQueryWrapper<AssetBorrow>()
            .eq(AssetBorrow::getStatus, "BORROWED")
            .lt(AssetBorrow::getExpectedReturnDate, now)
        );
        
        // 更新逾期状态
        overdueList.forEach(borrow -> {
            borrow.setStatus("OVERDUE");
            updateById(borrow);
        });
        
        return R.success(overdueList);
    }
}

