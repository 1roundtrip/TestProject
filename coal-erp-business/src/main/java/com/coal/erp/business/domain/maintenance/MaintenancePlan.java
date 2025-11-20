package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预防性维护计划表
 */
@Data
@TableName("maintenance_plan")
public class MaintenancePlan implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long planId;
    
    private String planNo;
    
    private String planName;
    
    private String planType;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String maintenanceType;
    
    private String cycleType;
    
    private Integer cycleValue;
    
    private String cycleUnit;
    
    private Date nextMaintenanceDate;
    
    private Date lastMaintenanceDate;
    
    private String maintenanceContent;
    
    private String requiredTools;
    
    private String requiredMaterials;
    
    private Integer estimatedDuration;
    
    private BigDecimal estimatedCost;
    
    private String status;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

