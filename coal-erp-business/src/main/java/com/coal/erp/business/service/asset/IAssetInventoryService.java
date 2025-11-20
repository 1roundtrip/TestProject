package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.asset.AssetInventory;
import com.coal.erp.business.domain.asset.AssetInventoryDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 资产盘点服务接口
 */
public interface IAssetInventoryService extends IService<AssetInventory> {
    
    /**
     * 创建盘点单
     */
    R<?> createInventory(AssetInventory inventory);
    
    /**
     * 添加盘点明细
     */
    R<?> addInventoryDetail(Long inventoryId, List<AssetInventoryDetail> details);
    
    /**
     * 开始盘点
     */
    R<?> startInventory(Long inventoryId);
    
    /**
     * 完成盘点
     */
    R<?> completeInventory(Long inventoryId);
    
    /**
     * 确认盘点
     */
    R<?> confirmInventory(Long inventoryId);
    
    /**
     * 处理盘点差异
     */
    R<?> handleDifference(Long detailId, String handleRemark);
    
    /**
     * 分页查询
     */
    Page<AssetInventory> pageInventory(Long current, Long size, String inventoryNo, String status);
    
    /**
     * 获取盘点明细
     */
    List<AssetInventoryDetail> getInventoryDetails(Long inventoryId);
}

