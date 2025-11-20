package com.coal.erp.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警记录表
 */
@Data
@TableName("warning_alert")
public class WarningAlert implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long alertId;
    
    private String alertType;
    
    private String alertLevel;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String alertTitle;
    
    private String alertContent;
    
    private Date expireDate;
    
    private Integer daysRemaining;
    
    private String status;
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















