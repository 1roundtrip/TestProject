package com.coal.erp.business.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 业务事件发布器
 */
@Component
public class BusinessEventPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(BusinessEventPublisher.class);
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    /**
     * 发布业务事件
     */
    public void publishEvent(BusinessEvent event) {
        try {
            log.info("发布业务事件: eventType={}, businessId={}, businessNo={}", 
                event.getEventType(), event.getBusinessId(), event.getBusinessNo());
            applicationEventPublisher.publishEvent(event);
        } catch (Exception e) {
            log.error("发布业务事件失败", e);
            throw new RuntimeException("发布业务事件失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 发布采购到资产流程事件
     */
    public void publishPurchaseToAssetEvent(PurchaseToAssetEvent event) {
        publishEvent(event);
    }
    
    /**
     * 发布维修业务流事件
     */
    public void publishMaintenanceBusinessEvent(MaintenanceBusinessEvent event) {
        publishEvent(event);
    }
    
    /**
     * 发布库存管理流事件
     */
    public void publishInventoryBusinessEvent(InventoryBusinessEvent event) {
        publishEvent(event);
    }
    
    /**
     * 发布预警处理流事件
     */
    public void publishWarningProcessEvent(WarningProcessEvent event) {
        publishEvent(event);
    }
}

