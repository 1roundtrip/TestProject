package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.asset.AssetTransfer;
import com.coal.erp.common.core.domain.R;

/**
 * 资产转移调拨服务接口
 */
public interface IAssetTransferService extends IService<AssetTransfer> {
    
    /**
     * 创建转移单
     */
    R<?> createTransfer(AssetTransfer transfer);
    
    /**
     * 审批转移单
     */
    R<?> approveTransfer(Long transferId, String approveRemark);
    
    /**
     * 驳回转移单
     */
    R<?> rejectTransfer(Long transferId, String rejectRemark);
    
    /**
     * 执行转移
     */
    R<?> executeTransfer(Long transferId);
    
    /**
     * 分页查询
     */
    Page<AssetTransfer> pageTransfer(Long current, Long size, String transferNo, String status);
}

