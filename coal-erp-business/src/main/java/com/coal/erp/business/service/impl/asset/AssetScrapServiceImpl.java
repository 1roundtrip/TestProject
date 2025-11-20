package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.asset.AssetScrap;
import com.coal.erp.business.mapper.AssetMapper;
import com.coal.erp.business.mapper.asset.AssetScrapMapper;
import com.coal.erp.business.service.asset.IAssetScrapService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 资产报废服务实现
 */
@Service
public class AssetScrapServiceImpl extends ServiceImpl<AssetScrapMapper, AssetScrap> 
        implements IAssetScrapService {
    
    @Autowired
    private AssetMapper assetMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createScrap(AssetScrap scrap) {
        // 检查资产是否存在
        Asset asset = assetMapper.selectById(scrap.getAssetId());
        if (asset == null) {
            return R.error("资产不存在");
        }
        
        // 生成报废单号
        scrap.setScrapNo("BF" + System.currentTimeMillis());
        scrap.setStatus("PENDING");
        scrap.setAssetCode(asset.getAssetCode());
        scrap.setAssetName(asset.getAssetName());
        scrap.setOriginalValue(asset.getPurchasePrice());
        
        // 获取资产净值（如果有折旧信息）
        // 这里简化处理，实际应该从折旧表获取
        scrap.setNetValue(asset.getPurchasePrice());
        
        scrap.setApplyUserId(SecurityUtils.getUserId());
        scrap.setApplyUserName(SecurityUtils.getUsername());
        scrap.setApplyTime(new Date());
        scrap.setCreateTime(new Date());
        
        save(scrap);
        return R.success(scrap);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveScrap(Long scrapId, String approveRemark) {
        AssetScrap scrap = getById(scrapId);
        if (scrap == null) {
            return R.error("报废单不存在");
        }
        if (!"PENDING".equals(scrap.getStatus())) {
            return R.error("只能审批待审批状态的报废单");
        }
        
        scrap.setStatus("APPROVED");
        scrap.setApproveUserId(SecurityUtils.getUserId());
        scrap.setApproveUserName(SecurityUtils.getUsername());
        scrap.setApproveTime(new Date());
        scrap.setApproveRemark(approveRemark);
        scrap.setUpdateTime(new Date());
        updateById(scrap);
        
        return R.success("审批成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> rejectScrap(Long scrapId, String rejectRemark) {
        AssetScrap scrap = getById(scrapId);
        if (scrap == null) {
            return R.error("报废单不存在");
        }
        if (!"PENDING".equals(scrap.getStatus())) {
            return R.error("只能驳回待审批状态的报废单");
        }
        
        scrap.setStatus("REJECTED");
        scrap.setApproveUserId(SecurityUtils.getUserId());
        scrap.setApproveUserName(SecurityUtils.getUsername());
        scrap.setApproveTime(new Date());
        scrap.setApproveRemark(rejectRemark);
        scrap.setUpdateTime(new Date());
        updateById(scrap);
        
        return R.success("驳回成功");
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> completeScrap(Long scrapId) {
        AssetScrap scrap = getById(scrapId);
        if (scrap == null) {
            return R.error("报废单不存在");
        }
        if (!"APPROVED".equals(scrap.getStatus())) {
            return R.error("只能完成已审批的报废单");
        }
        
        // 更新资产状态为报废
        Asset asset = assetMapper.selectById(scrap.getAssetId());
        if (asset != null) {
            asset.setStatus("2"); // 2-报废
            asset.setUpdateTime(new Date());
            assetMapper.updateById(asset);
        }
        
        scrap.setStatus("COMPLETED");
        scrap.setHandleUserId(SecurityUtils.getUserId());
        scrap.setHandleUserName(SecurityUtils.getUsername());
        scrap.setHandleTime(new Date());
        scrap.setUpdateTime(new Date());
        updateById(scrap);
        
        return R.success("报废完成");
    }
    
    @Override
    public Page<AssetScrap> pageScrap(Long current, Long size, String scrapNo, String status) {
        Page<AssetScrap> page = new Page<>(current, size);
        LambdaQueryWrapper<AssetScrap> wrapper = new LambdaQueryWrapper<>();
        if (scrapNo != null && !scrapNo.isEmpty()) {
            wrapper.like(AssetScrap::getScrapNo, scrapNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(AssetScrap::getStatus, status);
        }
        wrapper.orderByDesc(AssetScrap::getCreateTime);
        return page(page, wrapper);
    }
}

