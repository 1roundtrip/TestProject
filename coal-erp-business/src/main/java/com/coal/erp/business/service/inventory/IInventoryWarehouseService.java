package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryWarehouse;
import com.coal.erp.common.core.domain.R;

/**
 * 仓库服务接口
 */
public interface IInventoryWarehouseService extends IService<InventoryWarehouse> {
    
    /**
     * 创建仓库
     */
    R<?> createWarehouse(InventoryWarehouse warehouse);
    
    /**
     * 更新仓库
     */
    R<?> updateWarehouse(InventoryWarehouse warehouse);
    
    /**
     * 分页查询仓库
     */
    Page<InventoryWarehouse> pageWarehouse(Long current, Long size, String warehouseCode, String warehouseName, String status);
    
    /**
     * 检查仓库编码是否唯一
     */
    boolean checkWarehouseCodeUnique(InventoryWarehouse warehouse);
}

