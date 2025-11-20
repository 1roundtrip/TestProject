package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存物品表
 */
@Data
@TableName("inventory_material")
public class InventoryMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private String materialType;
    
    private String category;
    
    private String specification;
    
    private String brand;
    
    private String manufacturer;
    
    private String unit;
    
    private BigDecimal unitPrice;
    
    private String currency;
    
    private BigDecimal minStock;
    
    private BigDecimal maxStock;
    
    private BigDecimal safetyStock;
    
    private BigDecimal reorderPoint;
    
    private BigDecimal reorderQuantity;
    
    private Integer shelfLife;
    
    private String storageCondition;
    
    private String status;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

