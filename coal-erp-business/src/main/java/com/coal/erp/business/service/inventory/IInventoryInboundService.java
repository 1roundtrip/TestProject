package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryInbound;
import com.coal.erp.business.domain.inventory.InventoryInboundDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 入库服务接口
 */
public interface IInventoryInboundService extends IService<InventoryInbound> {
    
    /**
     * 创建入库单
     */
    R<?> createInbound(InventoryInbound inbound, List<InventoryInboundDetail> details);
    
    /**
     * 更新入库单
     */
    R<?> updateInbound(InventoryInbound inbound, List<InventoryInboundDetail> details);
    
    /**
     * 分页查询入库单
     */
    Page<InventoryInbound> pageInbound(Long current, Long size, String inboundNo, String inboundType, String status, Long warehouseId);
    
    /**
     * 提交审批
     */
    R<?> submitInbound(Long inboundId);
    
    /**
     * 审批入库单
     */
    R<?> approveInbound(Long inboundId);
    
    /**
     * 收货确认
     */
    R<?> receiveInbound(Long inboundId);
    
    /**
     * 获取入库明细
     */
    List<InventoryInboundDetail> getInboundDetails(Long inboundId);
}

