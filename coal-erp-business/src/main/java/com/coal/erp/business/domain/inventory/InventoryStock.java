package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存明细表
 */
@Data
@TableName("inventory_stock")
public class InventoryStock implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long stockId;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private Long locationId;
    
    private String locationCode;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private String batchNo;
    
    private Date productionDate;
    
    private Date expiryDate;
    
    private BigDecimal quantity;
    
    private BigDecimal availableQuantity;
    
    private BigDecimal frozenQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalValue;
    
    private Date lastInDate;
    
    private Date lastOutDate;
    
    private Date createTime;
    
    private Date updateTime;
}

