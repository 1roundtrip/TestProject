package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存调整明细表
 */
@Data
@TableName("inventory_adjustment_detail")
public class InventoryAdjustmentDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long adjustmentId;
    
    private Long stockId;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private Long locationId;
    
    private String locationCode;
    
    private String batchNo;
    
    private BigDecimal beforeQuantity;
    
    private BigDecimal afterQuantity;
    
    private BigDecimal adjustmentQuantity;
    
    private BigDecimal beforeUnitPrice;
    
    private BigDecimal afterUnitPrice;
    
    private BigDecimal beforeTotalValue;
    
    private BigDecimal afterTotalValue;
    
    private BigDecimal adjustmentValue;
    
    private String reason;
    
    private String remark;
}

