package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryStock;
import com.coal.erp.common.core.domain.R;

/**
 * 库存明细服务接口
 */
public interface IInventoryStockService extends IService<InventoryStock> {
    
    /**
     * 分页查询库存
     */
    Page<InventoryStock> pageStock(Long current, Long size, Long warehouseId, Long locationId, String materialCode, String materialName);
    
    /**
     * 获取物料库存汇总
     */
    R<?> getMaterialStockSummary(Long materialId);
    
    /**
     * 获取仓库库存汇总
     */
    R<?> getWarehouseStockSummary(Long warehouseId);
}

