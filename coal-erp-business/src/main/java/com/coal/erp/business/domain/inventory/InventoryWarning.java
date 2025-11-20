package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存预警记录表
 */
@Data
@TableName("inventory_warning")
public class InventoryWarning implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long warningId;
    
    private String warningNo;
    
    private String warningType;
    
    private String warningLevel;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private BigDecimal currentQuantity;
    
    private BigDecimal minStock;
    
    private BigDecimal maxStock;
    
    private BigDecimal safetyStock;
    
    private Date expiryDate;
    
    private Integer daysToExpiry;
    
    private String warningMessage;
    
    private String status;
    
    private Long handlerId;
    
    private String handlerName;
    
    private Date handleTime;
    
    private String handleResult;
    
    private Date warningTime;
    
    private Date createTime;
    
    private Date updateTime;
}

