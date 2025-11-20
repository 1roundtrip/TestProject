package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修绩效考核表
 */
@Data
@TableName("maintenance_performance")
public class MaintenancePerformance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long performanceId;
    
    private String evaluationPeriod;
    
    private Date evaluationDate;
    
    private Long evaluatedUserId;
    
    private String evaluatedUserName;
    
    private Long teamId;
    
    private String teamName;
    
    private Integer workOrderCount;
    
    private Integer completedCount;
    
    private BigDecimal completionRate;
    
    private BigDecimal averageCompletionTime;
    
    private BigDecimal qualityScore;
    
    private BigDecimal customerSatisfaction;
    
    private BigDecimal costEfficiency;
    
    private BigDecimal totalScore;
    
    private String performanceLevel;
    
    private Long evaluatorId;
    
    private String evaluatorName;
    
    private String evaluationComment;
    
    private Date createTime;
    
    private Date updateTime;
}

