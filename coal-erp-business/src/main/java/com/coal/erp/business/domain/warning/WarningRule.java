package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警规则表
 */
@Data
@TableName("warning_rule")
public class WarningRule implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long ruleId;
    
    private String ruleCode;
    
    private String ruleName;
    
    private String ruleType;
    
    private String warningCategory;
    
    private Long warningLevelId;
    
    private String ruleCondition;
    
    private String ruleExpression;
    
    private String checkFrequency;
    
    private Integer isEnabled;
    
    private Integer priority;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

