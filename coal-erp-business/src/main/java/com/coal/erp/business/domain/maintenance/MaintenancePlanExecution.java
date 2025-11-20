package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维护计划执行记录表
 */
@Data
@TableName("maintenance_plan_execution")
public class MaintenancePlanExecution implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long executionId;
    
    private Long planId;
    
    private String planNo;
    
    private Long workOrderId;
    
    private String workOrderNo;
    
    private Date scheduledDate;
    
    private Date actualDate;
    
    private Long executedBy;
    
    private String executedByName;
    
    private String executionStatus;
    
    private Integer actualDuration;
    
    private BigDecimal actualCost;
    
    private BigDecimal qualityScore;
    
    private String executionComment;
    
    private Date createTime;
    
    private Date updateTime;
}

