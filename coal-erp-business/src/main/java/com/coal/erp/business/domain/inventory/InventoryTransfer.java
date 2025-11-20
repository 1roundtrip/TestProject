package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 调拨单表
 */
@Data
@TableName("inventory_transfer")
public class InventoryTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long transferId;
    
    private String transferNo;
    
    private String transferType;
    
    private Long fromWarehouseId;
    
    private String fromWarehouseCode;
    
    private String fromWarehouseName;
    
    private Long fromLocationId;
    
    private String fromLocationCode;
    
    private Long toWarehouseId;
    
    private String toWarehouseCode;
    
    private String toWarehouseName;
    
    private Long toLocationId;
    
    private String toLocationCode;
    
    private Date transferDate;
    
    private BigDecimal totalQuantity;
    
    private BigDecimal totalAmount;
    
    private Long handlerId;
    
    private String handlerName;
    
    private String status;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Date outboundTime;
    
    private Date inboundTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

