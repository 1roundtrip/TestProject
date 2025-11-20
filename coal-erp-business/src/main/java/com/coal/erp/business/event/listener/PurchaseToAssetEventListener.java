package com.coal.erp.business.event.listener;

import com.coal.erp.business.event.PurchaseToAssetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 采购到资产流程事件监听器
 */
@Component
public class PurchaseToAssetEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(PurchaseToAssetEventListener.class);
    
    /**
     * 监听采购收货确认事件，自动创建资产入库单
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleReceivingConfirmed(PurchaseToAssetEvent event) {
        try {
            if ("RECEIVING_CONFIRMED".equals(event.getStep())) {
                log.info("处理采购收货确认事件: receivingId={}, receivingNo={}", 
                    event.getReceivingId(), event.getReceivingNo());
                
                // 这里会调用业务集成服务处理
                // 实际逻辑在 BusinessIntegrationService 中实现
            }
        } catch (Exception e) {
            log.error("处理采购收货确认事件失败", e);
        }
    }
    
    /**
     * 监听资产入库完成事件，自动创建财务付款单
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleAssetStorageCompleted(PurchaseToAssetEvent event) {
        try {
            if ("ASSET_STORAGE_COMPLETED".equals(event.getStep())) {
                log.info("处理资产入库完成事件: assetStorageId={}, assetStorageNo={}", 
                    event.getAssetStorageId(), event.getAssetStorageNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理资产入库完成事件失败", e);
        }
    }
}

