package com.coal.erp.business.service.inventory;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.inventory.InventoryStatistics;
import com.coal.erp.common.core.domain.R;

import java.util.Date;

/**
 * 库存报表服务接口
 */
public interface IInventoryReportService extends IService<InventoryStatistics> {
    
    /**
     * 获取库存汇总报表
     */
    R<?> getStockSummaryReport(Long warehouseId, Date startDate, Date endDate);
    
    /**
     * 获取入库出库统计
     */
    R<?> getInboundOutboundStatistics(Long warehouseId, Date startDate, Date endDate);
    
    /**
     * 获取库存周转率分析
     */
    R<?> getTurnoverRateAnalysis(Long warehouseId, Date startDate, Date endDate);
    
    /**
     * 获取物料ABC分析
     */
    R<?> getABCAnalysis(Long warehouseId);
    
    /**
     * 获取库存价值分析
     */
    R<?> getStockValueAnalysis(Long warehouseId, Date asOfDate);
    
    /**
     * 分页查询库存统计报表
     */
    R<?> getReportPage(Long current, Long size, String startDate, String endDate, Long warehouseId, String statType);
    
    /**
     * 获取库存统计汇总数据
     */
    R<?> getReportStatistics(String startDate, String endDate, Long warehouseId);
}

