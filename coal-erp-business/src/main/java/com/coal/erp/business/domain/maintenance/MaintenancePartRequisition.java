package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修备件领用表
 */
@Data
@TableName("maintenance_part_requisition")
public class MaintenancePartRequisition implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long requisitionId;
    
    private String requisitionNo;
    
    private Long workOrderId;
    
    private String workOrderNo;
    
    private String requisitionType;
    
    private Date requisitionDate;
    
    private Long requisitionBy;
    
    private String requisitionByName;
    
    private Long warehouseId;
    
    private String warehouseName;
    
    private BigDecimal totalAmount;
    
    private String status;
    
    private Long approveBy;
    
    private String approveByName;
    
    private Date approveTime;
    
    private Long issueBy;
    
    private String issueByName;
    
    private Date issueTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

