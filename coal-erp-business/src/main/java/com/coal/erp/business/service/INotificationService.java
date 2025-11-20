package com.coal.erp.business.service;

import com.coal.erp.business.domain.WarningAlert;

import java.util.List;

/**
 * 通知服务接口
 */
public interface INotificationService {
    
    /**
     * 发送系统消息通知
     */
    void sendSystemNotification(WarningAlert alert);
    
    /**
     * 批量发送系统消息通知
     */
    void sendBatchSystemNotification(List<WarningAlert> alerts);
    
    /**
     * 发送邮件通知（可选）
     */
    void sendEmailNotification(WarningAlert alert, String email);
}















