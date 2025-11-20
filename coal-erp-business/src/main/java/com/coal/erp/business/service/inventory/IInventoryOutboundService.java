package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryOutbound;
import com.coal.erp.business.domain.inventory.InventoryOutboundDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 出库服务接口
 */
public interface IInventoryOutboundService extends IService<InventoryOutbound> {
    
    /**
     * 创建出库单
     */
    R<?> createOutbound(InventoryOutbound outbound, List<InventoryOutboundDetail> details);
    
    /**
     * 更新出库单
     */
    R<?> updateOutbound(InventoryOutbound outbound, List<InventoryOutboundDetail> details);
    
    /**
     * 分页查询出库单
     */
    Page<InventoryOutbound> pageOutbound(Long current, Long size, String outboundNo, String outboundType, String status, Long warehouseId);
    
    /**
     * 提交审批
     */
    R<?> submitOutbound(Long outboundId);
    
    /**
     * 审批出库单
     */
    R<?> approveOutbound(Long outboundId);
    
    /**
     * 发放出库
     */
    R<?> issueOutbound(Long outboundId);
    
    /**
     * 获取出库明细
     */
    List<InventoryOutboundDetail> getOutboundDetails(Long outboundId);
}

