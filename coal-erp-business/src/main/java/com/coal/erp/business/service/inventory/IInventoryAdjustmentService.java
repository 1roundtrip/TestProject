package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryAdjustment;
import com.coal.erp.business.domain.inventory.InventoryAdjustmentDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 库存调整服务接口
 */
public interface IInventoryAdjustmentService extends IService<InventoryAdjustment> {
    
    /**
     * 创建调整单
     */
    R<?> createAdjustment(InventoryAdjustment adjustment, List<InventoryAdjustmentDetail> details);
    
    /**
     * 分页查询调整单
     */
    Page<InventoryAdjustment> pageAdjustment(Long current, Long size, String adjustmentNo, String adjustmentType, String status, Long warehouseId);
    
    /**
     * 提交审批
     */
    R<?> submitAdjustment(Long adjustmentId);
    
    /**
     * 审批调整单
     */
    R<?> approveAdjustment(Long adjustmentId);
    
    /**
     * 获取调整明细
     */
    List<InventoryAdjustmentDetail> getAdjustmentDetails(Long adjustmentId);
}

