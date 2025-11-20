package com.coal.erp.business.event.listener;

import com.coal.erp.business.event.WarningProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 预警处理流事件监听器
 */
@Component
public class WarningProcessEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(WarningProcessEventListener.class);
    
    /**
     * 监听预警触发事件，自动分发通知
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleWarningTriggered(WarningProcessEvent event) {
        try {
            if ("WARNING_TRIGGERED".equals(event.getStatus())) {
                log.info("处理预警触发事件: recordId={}, warningType={}, warningLevel={}", 
                    event.getRecordId(), event.getWarningType(), event.getWarningLevel());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理预警触发事件失败", e);
        }
    }
    
    /**
     * 监听通知分发完成事件，记录处理跟踪
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleNotificationDistributed(WarningProcessEvent event) {
        try {
            if ("NOTIFICATION_DISTRIBUTED".equals(event.getStatus())) {
                log.info("处理通知分发完成事件: notificationId={}, recordId={}", 
                    event.getNotificationId(), event.getRecordId());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理通知分发完成事件失败", e);
        }
    }
    
    /**
     * 监听预警处理完成事件，反馈处理结果
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleWarningHandled(WarningProcessEvent event) {
        try {
            if ("WARNING_HANDLED".equals(event.getStatus())) {
                log.info("处理预警处理完成事件: handleRecordId={}, handleResult={}", 
                    event.getHandleRecordId(), event.getHandleResult());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理预警处理完成事件失败", e);
        }
    }
    
    /**
     * 监听预警升级事件
     */
    @EventListener
    @Async("businessEventExecutor")
    public void handleWarningEscalated(WarningProcessEvent event) {
        try {
            if (event.getNeedEscalation() != null && event.getNeedEscalation()) {
                log.info("处理预警升级事件: recordId={}, escalationLevel={}", 
                    event.getRecordId(), event.getEscalationLevel());
                
                // 这里会调用业务集成服务处理
            }
        } catch (Exception e) {
            log.error("处理预警升级事件失败", e);
        }
    }
}

