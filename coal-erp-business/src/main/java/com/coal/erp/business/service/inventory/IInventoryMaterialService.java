package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryMaterial;
import com.coal.erp.common.core.domain.R;

/**
 * 库存物品服务接口
 */
public interface IInventoryMaterialService extends IService<InventoryMaterial> {
    
    /**
     * 创建物料
     */
    R<?> createMaterial(InventoryMaterial material);
    
    /**
     * 更新物料
     */
    R<?> updateMaterial(InventoryMaterial material);
    
    /**
     * 分页查询物料
     */
    Page<InventoryMaterial> pageMaterial(Long current, Long size, String materialCode, String materialName, String materialType, String status);
    
    /**
     * 检查物料编码是否唯一
     */
    boolean checkMaterialCodeUnique(InventoryMaterial material);
}

