package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点明细表
 */
@Data
@TableName("inventory_stocktaking_detail")
public class InventoryStocktakingDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long stocktakingId;
    
    private Long stockId;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private Long locationId;
    
    private String locationCode;
    
    private String batchNo;
    
    private BigDecimal bookQuantity;
    
    private BigDecimal actualQuantity;
    
    private BigDecimal differenceQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal differenceAmount;
    
    private String differenceType;
    
    private String reason;
    
    private String remark;
}

