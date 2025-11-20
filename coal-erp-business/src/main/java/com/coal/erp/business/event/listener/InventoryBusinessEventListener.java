package com.coal.erp.business.event.listener;

import com.coal.erp.business.event.InventoryBusinessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 库存管理流事件监听器
 */
@Component
public class InventoryBusinessEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(InventoryBusinessEventListener.class);
    
    /**
     * 监听安全库存预警事件，自动创建采购申请
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleSafetyStockWarning(InventoryBusinessEvent event) {
        try {
            if ("SAFETY_STOCK_WARNING".equals(event.getStep())) {
                log.info("处理安全库存预警事件: warningId={}, warehouseId={}", 
                    event.getWarningId(), event.getWarehouseId());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理安全库存预警事件失败", e);
        }
    }
    
    /**
     * 监听采购申请创建事件，更新预警状态
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handlePurchaseRequisitionCreated(InventoryBusinessEvent event) {
        try {
            if ("PURCHASE_REQUISITION_CREATED".equals(event.getStep())) {
                log.info("处理采购申请创建事件: purchaseRequisitionId={}, purchaseRequisitionNo={}", 
                    event.getPurchaseRequisitionId(), event.getPurchaseRequisitionNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理采购申请创建事件失败", e);
        }
    }
    
    /**
     * 监听库存补充完成事件，更新库存数量
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleStockReplenished(InventoryBusinessEvent event) {
        try {
            if ("STOCK_REPLENISHED".equals(event.getStep())) {
                log.info("处理库存补充完成事件: inboundId={}, inboundNo={}", 
                    event.getInboundId(), event.getInboundNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理库存补充完成事件失败", e);
        }
    }
    
    /**
     * 监听资产领用事件，更新库存数量
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleAssetRequisition(InventoryBusinessEvent event) {
        try {
            if ("ASSET_REQUISITION".equals(event.getStep())) {
                log.info("处理资产领用事件: outboundId={}, outboundNo={}", 
                    event.getOutboundId(), event.getOutboundNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理资产领用事件失败", e);
        }
    }
}

