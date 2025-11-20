package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警级别表
 */
@Data
@TableName("warning_level")
public class WarningLevel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long levelId;
    
    private String levelCode;
    
    private String levelName;
    
    private String levelColor;
    
    private Integer levelOrder;
    
    private String notificationChannels;
    
    private String escalationRule;
    
    private Integer isEnabled;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

