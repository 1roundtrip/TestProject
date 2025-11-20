package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryTransfer;
import com.coal.erp.business.domain.inventory.InventoryTransferDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 库存调拨服务接口
 */
public interface IInventoryTransferService extends IService<InventoryTransfer> {
    
    /**
     * 创建调拨单
     */
    R<?> createTransfer(InventoryTransfer transfer, List<InventoryTransferDetail> details);
    
    /**
     * 分页查询调拨单
     */
    Page<InventoryTransfer> pageTransfer(Long current, Long size, String transferNo, String status, Long fromWarehouseId, Long toWarehouseId);
    
    /**
     * 提交审批
     */
    R<?> submitTransfer(Long transferId);
    
    /**
     * 审批调拨单
     */
    R<?> approveTransfer(Long transferId);
    
    /**
     * 出库确认
     */
    R<?> outboundTransfer(Long transferId);
    
    /**
     * 入库确认
     */
    R<?> inboundTransfer(Long transferId);
    
    /**
     * 获取调拨明细
     */
    List<InventoryTransferDetail> getTransferDetails(Long transferId);
}

