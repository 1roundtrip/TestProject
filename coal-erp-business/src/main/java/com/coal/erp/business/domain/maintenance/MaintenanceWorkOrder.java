package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修工单表
 */
@Data
@TableName("maintenance_work_order")
public class MaintenanceWorkOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long workOrderId;
    
    private String workOrderNo;
    
    private String workOrderType;
    
    private String priority;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String faultType;
    
    private String faultDescription;
    
    private Long reportedBy;
    
    private String reportedByName;
    
    private Date reportedTime;
    
    private Long assignedTeamId;
    
    private String assignedTeamName;
    
    private Long assignedTechnicianId;
    
    private String assignedTechnicianName;
    
    private Date scheduledStartTime;
    
    private Date scheduledEndTime;
    
    private Date actualStartTime;
    
    private Date actualEndTime;
    
    private String status;
    
    private BigDecimal completionRate;
    
    private BigDecimal laborCost;
    
    private BigDecimal materialCost;
    
    private BigDecimal totalCost;
    
    private BigDecimal qualityScore;
    
    private String qualityComment;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

