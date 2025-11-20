package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryStocktaking;
import com.coal.erp.business.domain.inventory.InventoryStocktakingDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 库存盘点服务接口
 */
public interface IInventoryStocktakingService extends IService<InventoryStocktaking> {
    
    /**
     * 创建盘点单
     */
    R<?> createStocktaking(InventoryStocktaking stocktaking, List<InventoryStocktakingDetail> details);
    
    /**
     * 分页查询盘点单
     */
    Page<InventoryStocktaking> pageStocktaking(Long current, Long size, String stocktakingNo, String status, Long warehouseId);
    
    /**
     * 开始盘点
     */
    R<?> startStocktaking(Long stocktakingId);
    
    /**
     * 完成盘点
     */
    R<?> completeStocktaking(Long stocktakingId);
    
    /**
     * 确认盘点
     */
    R<?> confirmStocktaking(Long stocktakingId);
    
    /**
     * 获取盘点明细
     */
    List<InventoryStocktakingDetail> getStocktakingDetails(Long stocktakingId);
}

