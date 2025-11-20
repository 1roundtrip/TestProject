package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备故障记录表
 */
@Data
@TableName("maintenance_fault_record")
public class MaintenanceFaultRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long faultId;
    
    private String faultNo;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String faultType;
    
    private String faultCategory;
    
    private String faultSeverity;
    
    private String faultDescription;
    
    private String faultCause;
    
    private String faultSymptom;
    
    private Date occurredTime;
    
    private Date reportedTime;
    
    private Date resolvedTime;
    
    private Integer downtime;
    
    private Long workOrderId;
    
    private String workOrderNo;
    
    private String resolutionMethod;
    
    private String preventionMeasure;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

