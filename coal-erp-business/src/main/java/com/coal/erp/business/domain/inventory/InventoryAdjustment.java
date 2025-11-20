package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存调整单表
 */
@Data
@TableName("inventory_adjustment")
public class InventoryAdjustment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long adjustmentId;
    
    private String adjustmentNo;
    
    private String adjustmentType;
    
    private String adjustmentReason;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private Date adjustmentDate;
    
    private Integer totalItems;
    
    private Long handlerId;
    
    private String handlerName;
    
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

