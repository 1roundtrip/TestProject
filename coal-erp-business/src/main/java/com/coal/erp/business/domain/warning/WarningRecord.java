package com.coal.erp.business.domain.warning;

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
@TableName("warning_record")
public class WarningRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long recordId;
    
    private Long ruleId;
    
    private String ruleCode;
    
    private String ruleName;
    
    private String warningType;
    
    private String warningCategory;
    
    private Long warningLevelId;
    
    private String warningLevelCode;
    
    private String warningLevelName;
    
    private String warningTitle;
    
    private String warningContent;
    
    private String warningData;
    
    private String sourceType;
    
    private Long sourceId;
    
    private String sourceCode;
    
    private String sourceName;
    
    private Date triggerTime;
    
    private String status;
    
    private Long handlerId;
    
    private String handlerName;
    
    private Date handleTime;
    
    private String handleResult;
    
    private Date resolveTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

