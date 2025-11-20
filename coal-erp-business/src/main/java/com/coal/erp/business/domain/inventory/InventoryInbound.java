package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库单表
 */
@Data
@TableName("inventory_inbound")
public class InventoryInbound implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long inboundId;
    
    private String inboundNo;
    
    private String inboundType;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private String sourceType;
    
    private String sourceNo;
    
    private Long sourceId;
    
    private Date inboundDate;
    
    private Long supplierId;
    
    private String supplierName;
    
    private BigDecimal totalQuantity;
    
    private BigDecimal totalAmount;
    
    private Long handlerId;
    
    private String handlerName;
    
    private Long receiverId;
    
    private String receiverName;
    
    private String status;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

