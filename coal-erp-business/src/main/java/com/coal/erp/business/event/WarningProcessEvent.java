package com.coal.erp.business.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 预警处理流事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WarningProcessEvent extends BusinessEvent {
    
    /**
     * 预警规则ID
     */
    private Long ruleId;
    
    /**
     * 预警规则编码
     */
    private String ruleCode;
    
    /**
     * 预警记录ID
     */
    private Long recordId;
    
    /**
     * 预警级别
     */
    private String warningLevel;
    
    /**
     * 预警类型
     */
    private String warningType;
    
    /**
     * 预警标题
     */
    private String warningTitle;
    
    /**
     * 预警内容
     */
    private String warningContent;
    
    /**
     * 通知ID
     */
    private Long notificationId;
    
    /**
     * 处理记录ID
     */
    private Long handleRecordId;
    
    /**
     * 处理状态
     */
    private String handleStatus;
    
    /**
     * 处理结果
     */
    private String handleResult;
    
    /**
     * 是否需要升级
     */
    private Boolean needEscalation;
    
    /**
     * 升级级别
     */
    private String escalationLevel;
    
    public WarningProcessEvent() {
        super();
        this.setEventType("WARNING_PROCESS");
        this.setBusinessCenter("WARNING");
    }
}

