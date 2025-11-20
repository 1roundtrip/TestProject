package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 出库单表
 */
@Data
@TableName("inventory_outbound")
public class InventoryOutbound implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long outboundId;
    
    private String outboundNo;
    
    private String outboundType;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private String destinationType;
    
    private String destinationNo;
    
    private Long destinationId;
    
    private Date outboundDate;
    
    private Long customerId;
    
    private String customerName;
    
    private Long deptId;
    
    private String deptName;
    
    private Long recipientId;
    
    private String recipientName;
    
    private BigDecimal totalQuantity;
    
    private BigDecimal totalAmount;
    
    private Long handlerId;
    
    private String handlerName;
    
    private String status;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long issueUserId;
    
    private String issueUserName;
    
    private Date issueTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

