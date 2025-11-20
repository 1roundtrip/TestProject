package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.asset.AssetBorrow;
import com.coal.erp.common.core.domain.R;

/**
 * 资产领用退库服务接口
 */
public interface IAssetBorrowService extends IService<AssetBorrow> {
    
    /**
     * 创建领用单
     */
    R<?> createBorrow(AssetBorrow borrow);
    
    /**
     * 退库
     */
    R<?> returnAsset(Long borrowId);
    
    /**
     * 分页查询
     */
    Page<AssetBorrow> pageBorrow(Long current, Long size, String borrowNo, String status, String borrowType);
    
    /**
     * 获取逾期领用列表
     */
    R<?> getOverdueBorrows();
}

