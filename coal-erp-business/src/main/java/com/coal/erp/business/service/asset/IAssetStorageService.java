package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.asset.AssetStorage;
import com.coal.erp.business.domain.asset.AssetStorageDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 资产入库服务接口
 */
public interface IAssetStorageService extends IService<AssetStorage> {
    
    /**
     * 创建入库单
     */
    R<?> createStorage(AssetStorage storage, List<AssetStorageDetail> details);
    
    /**
     * 确认入库
     */
    R<?> confirmStorage(Long storageId);
    
    /**
     * 取消入库
     */
    R<?> cancelStorage(Long storageId);
    
    /**
     * 分页查询
     */
    Page<AssetStorage> pageStorage(Long current, Long size, String storageNo, String status);
    
    /**
     * 获取入库明细
     */
    List<AssetStorageDetail> getStorageDetails(Long storageId);
}

