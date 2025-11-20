package com.coal.erp.business.event.listener;

import com.coal.erp.business.event.MaintenanceBusinessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 维修业务流事件监听器
 */
@Component
public class MaintenanceBusinessEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(MaintenanceBusinessEventListener.class);
    
    /**
     * 监听设备预警事件，自动创建维修工单
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleEquipmentWarning(MaintenanceBusinessEvent event) {
        try {
            if ("EQUIPMENT_WARNING".equals(event.getStep())) {
                log.info("处理设备预警事件: warningRecordId={}, equipmentId={}", 
                    event.getWarningRecordId(), event.getEquipmentId());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理设备预警事件失败", e);
        }
    }
    
    /**
     * 监听维修工单创建事件，自动创建备件领用单
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleWorkOrderCreated(MaintenanceBusinessEvent event) {
        try {
            if ("WORK_ORDER_CREATED".equals(event.getStep())) {
                log.info("处理维修工单创建事件: workOrderId={}, workOrderNo={}", 
                    event.getWorkOrderId(), event.getWorkOrderNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理维修工单创建事件失败", e);
        }
    }
    
    /**
     * 监听备件领用完成事件，自动更新库存
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handlePartRequisitionCompleted(MaintenanceBusinessEvent event) {
        try {
            if ("PART_REQUISITION_COMPLETED".equals(event.getStep())) {
                log.info("处理备件领用完成事件: requisitionId={}, requisitionNo={}", 
                    event.getRequisitionId(), event.getRequisitionNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理备件领用完成事件失败", e);
        }
    }
    
    /**
     * 监听维修完成事件，自动创建费用核算
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleMaintenanceCompleted(MaintenanceBusinessEvent event) {
        try {
            if ("MAINTENANCE_COMPLETED".equals(event.getStep())) {
                log.info("处理维修完成事件: workOrderId={}, workOrderNo={}", 
                    event.getWorkOrderId(), event.getWorkOrderNo());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理维修完成事件失败", e);
        }
    }
}

