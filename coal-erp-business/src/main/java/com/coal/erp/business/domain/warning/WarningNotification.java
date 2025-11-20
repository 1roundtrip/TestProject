package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警通知表
 */
@Data
@TableName("warning_notification")
public class WarningNotification implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long notificationId;
    
    private Long recordId;
    
    private String channelType;
    
    private Long recipientId;
    
    private String recipientName;
    
    private String recipientEmail;
    
    private String recipientPhone;
    
    private String notificationTitle;
    
    private String notificationContent;
    
    private Long templateId;
    
    private String sendStatus;
    
    private Date sendTime;
    
    private String sendResult;
    
    private Integer retryCount;
    
    private Date createTime;
    
    private Date updateTime;
}

