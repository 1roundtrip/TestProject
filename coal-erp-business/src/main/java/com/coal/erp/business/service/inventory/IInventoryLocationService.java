package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryLocation;
import com.coal.erp.common.core.domain.R;

/**
 * 库位服务接口
 */
public interface IInventoryLocationService extends IService<InventoryLocation> {
    
    /**
     * 创建库位
     */
    R<?> createLocation(InventoryLocation location);
    
    /**
     * 更新库位
     */
    R<?> updateLocation(InventoryLocation location);
    
    /**
     * 分页查询库位
     */
    Page<InventoryLocation> pageLocation(Long current, Long size, Long warehouseId, String locationCode, String status);
    
    /**
     * 检查库位编码是否唯一
     */
    boolean checkLocationCodeUnique(InventoryLocation location);
}

