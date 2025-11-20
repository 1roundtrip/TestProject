package com.coal.erp.business.event;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 业务事件基类
 */
@Data
public class BusinessEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 事件ID
     */
    private String eventId;
    
    /**
     * 事件类型
     */
    private String eventType;
    
    /**
     * 业务中心
     */
    private String businessCenter;
    
    /**
     * 业务ID
     */
    private Long businessId;
    
    /**
     * 业务编号
     */
    private String businessNo;
    
    /**
     * 事件状态
     */
    private String status;
    
    /**
     * 事件数据
     */
    private Map<String, Object> eventData;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 创建人ID
     */
    private Long createUserId;
    
    /**
     * 创建人姓名
     */
    private String createUserName;
    
    public BusinessEvent() {
        this.eventId = generateEventId();
        this.createTime = new Date();
    }
    
    private String generateEventId() {
        return "EVT" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
    }
}

