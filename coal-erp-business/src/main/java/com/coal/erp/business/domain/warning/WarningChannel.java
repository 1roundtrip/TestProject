package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警渠道配置表
 */
@Data
@TableName("warning_channel")
public class WarningChannel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long channelId;
    
    private String channelCode;
    
    private String channelName;
    
    private String channelType;
    
    private String channelConfig;
    
    private Integer isEnabled;
    
    private Integer priority;
    
    private Integer dailyLimit;
    
    private Integer currentCount;
    
    private Date resetTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

