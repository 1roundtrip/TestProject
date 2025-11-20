package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.asset.AssetDepreciation;
import com.coal.erp.business.domain.asset.AssetDepreciationDetail;
import com.coal.erp.business.mapper.AssetMapper;
import com.coal.erp.business.mapper.asset.AssetDepreciationDetailMapper;
import com.coal.erp.business.mapper.asset.AssetDepreciationMapper;
import com.coal.erp.business.service.asset.IAssetDepreciationService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * 资产折旧服务实现
 */
@Service
public class AssetDepreciationServiceImpl extends ServiceImpl<AssetDepreciationMapper, AssetDepreciation> 
        implements IAssetDepreciationService {
    
    @Autowired
    private AssetDepreciationDetailMapper detailMapper;
    
    @Autowired
    private AssetMapper assetMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> configDepreciation(AssetDepreciation depreciation) {
        // 检查资产是否存在
        Asset asset = assetMapper.selectById(depreciation.getAssetId());
        if (asset == null) {
            return R.error("资产不存在");
        }
        
        // 检查是否已配置折旧
        AssetDepreciation existing = getOne(new LambdaQueryWrapper<AssetDepreciation>()
            .eq(AssetDepreciation::getAssetId, depreciation.getAssetId())
        );
        
        if (existing != null) {
            return R.error("该资产已配置折旧，请先删除再重新配置");
        }
        
        // 设置资产信息
        depreciation.setAssetCode(asset.getAssetCode());
        depreciation.setAssetName(asset.getAssetName());
        
        // 计算折旧率
        if ("STRAIGHT_LINE".equals(depreciation.getDepreciationMethod())) {
            // 直线法：月折旧率 = (1 - 残值率) / 使用年限（月）
            BigDecimal residualRate = depreciation.getResidualValue()
                .divide(depreciation.getOriginalValue(), 4, RoundingMode.HALF_UP);
            BigDecimal monthlyRate = BigDecimal.ONE.subtract(residualRate)
                .divide(new BigDecimal(depreciation.getUsefulLife()), 4, RoundingMode.HALF_UP);
            depreciation.setDepreciationRate(monthlyRate.multiply(new BigDecimal(100)));
            
            // 月折旧额 = (原值 - 残值) / 使用年限（月）
            depreciation.setMonthlyDepreciation(
                depreciation.getOriginalValue()
                    .subtract(depreciation.getResidualValue())
                    .divide(new BigDecimal(depreciation.getUsefulLife()), 2, RoundingMode.HALF_UP)
            );
        }
        
        depreciation.setAccumulatedDepreciation(BigDecimal.ZERO);
        depreciation.setNetValue(depreciation.getOriginalValue());
        depreciation.setStatus("ACTIVE");
        depreciation.setCreateTime(new Date());
        
        save(depreciation);
        
        // 更新资产（如果需要，可以添加折旧状态字段）
        asset.setUpdateTime(new Date());
        assetMapper.updateById(asset);
        
        return R.success(depreciation);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> calculateDepreciation(String month) {
        // 获取所有活跃的折旧配置
        List<AssetDepreciation> depreciations = list(new LambdaQueryWrapper<AssetDepreciation>()
            .eq(AssetDepreciation::getStatus, "ACTIVE")
        );
        
        int successCount = 0;
        int failCount = 0;
        
        for (AssetDepreciation dep : depreciations) {
            try {
                // 检查该月是否已计提
                AssetDepreciationDetail existing = detailMapper.selectOne(
                    new LambdaQueryWrapper<AssetDepreciationDetail>()
                        .eq(AssetDepreciationDetail::getDepreciationId, dep.getDepreciationId())
                        .eq(AssetDepreciationDetail::getDepreciationMonth, month)
                );
                
                if (existing != null) {
                    continue; // 已计提，跳过
                }
                
                // 计算累计折旧
                BigDecimal newAccumulated = dep.getAccumulatedDepreciation()
                    .add(dep.getMonthlyDepreciation());
                
                // 检查是否已提完
                BigDecimal maxDepreciation = dep.getOriginalValue()
                    .subtract(dep.getResidualValue());
                
                if (newAccumulated.compareTo(maxDepreciation) >= 0) {
                    // 已提完
                    dep.setStatus("COMPLETED");
                    dep.setAccumulatedDepreciation(maxDepreciation);
                    dep.setNetValue(dep.getResidualValue());
                } else {
                    dep.setAccumulatedDepreciation(newAccumulated);
                    dep.setNetValue(dep.getOriginalValue().subtract(newAccumulated));
                }
                
                dep.setLastDepreciationDate(new Date());
                updateById(dep);
                
                // 创建折旧明细
                AssetDepreciationDetail detail = new AssetDepreciationDetail();
                detail.setDepreciationId(dep.getDepreciationId());
                detail.setAssetId(dep.getAssetId());
                detail.setDepreciationMonth(month);
                detail.setDepreciationAmount(dep.getMonthlyDepreciation());
                detail.setAccumulatedAmount(dep.getAccumulatedDepreciation());
                detail.setNetValue(dep.getNetValue());
                detail.setStatus("PENDING");
                detail.setCreateTime(new Date());
                detailMapper.insert(detail);
                
                successCount++;
            } catch (Exception e) {
                failCount++;
                e.printStackTrace();
            }
        }
        
        return R.success(String.format("计提完成：成功 %d 条，失败 %d 条", successCount, failCount));
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmDepreciation(Long detailId) {
        AssetDepreciationDetail detail = detailMapper.selectById(detailId);
        if (detail == null) {
            return R.error("折旧明细不存在");
        }
        if (!"PENDING".equals(detail.getStatus())) {
            return R.error("只能确认待确认状态的折旧明细");
        }
        
        detail.setStatus("CONFIRMED");
        detail.setConfirmTime(new Date());
        detail.setConfirmUserId(SecurityUtils.getUserId());
        detailMapper.updateById(detail);
        
        return R.success("确认成功");
    }
    
    @Override
    public Page<AssetDepreciation> pageDepreciation(Long current, Long size, String assetCode, String status) {
        Page<AssetDepreciation> page = new Page<>(current, size);
        LambdaQueryWrapper<AssetDepreciation> wrapper = new LambdaQueryWrapper<>();
        if (assetCode != null && !assetCode.isEmpty()) {
            wrapper.like(AssetDepreciation::getAssetCode, assetCode);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(AssetDepreciation::getStatus, status);
        }
        wrapper.orderByDesc(AssetDepreciation::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<AssetDepreciationDetail> getDepreciationDetails(Long depreciationId) {
        LambdaQueryWrapper<AssetDepreciationDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetDepreciationDetail::getDepreciationId, depreciationId);
        wrapper.orderByDesc(AssetDepreciationDetail::getDepreciationMonth);
        return detailMapper.selectList(wrapper);
    }
    
    @Override
    public List<AssetDepreciationDetail> getMonthDepreciationDetails(String month) {
        LambdaQueryWrapper<AssetDepreciationDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssetDepreciationDetail::getDepreciationMonth, month);
        wrapper.orderByDesc(AssetDepreciationDetail::getDepreciationId);
        return detailMapper.selectList(wrapper);
    }
}