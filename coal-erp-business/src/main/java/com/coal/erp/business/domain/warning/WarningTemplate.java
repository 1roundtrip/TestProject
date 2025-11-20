package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警模板表
 */
@Data
@TableName("warning_template")
public class WarningTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long templateId;
    
    private String templateCode;
    
    private String templateName;
    
    private String templateType;
    
    private String warningType;
    
    private String templateSubject;
    
    private String templateContent;
    
    private String templateVariables;
    
    private Integer isDefault;
    
    private Integer isEnabled;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

