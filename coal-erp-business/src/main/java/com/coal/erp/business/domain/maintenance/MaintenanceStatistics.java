package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修统计汇总表
 */
@Data
@TableName("maintenance_statistics")
public class MaintenanceStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long statId;
    
    private Date statDate;
    
    private String statType;
    
    private Integer totalWorkOrders;
    
    private Integer completedWorkOrders;
    
    private Integer pendingWorkOrders;
    
    private BigDecimal averageCompletionTime;
    
    private BigDecimal totalLaborCost;
    
    private BigDecimal totalMaterialCost;
    
    private BigDecimal totalCost;
    
    private BigDecimal averageQualityScore;
    
    private Integer faultCount;
    
    private BigDecimal downtimeHours;
    
    private Date createTime;
    
    private Date updateTime;
}

