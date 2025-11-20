package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 调拨明细表
 */
@Data
@TableName("inventory_transfer_detail")
public class InventoryTransferDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long transferId;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal outboundQuantity;
    
    private BigDecimal inboundQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal amount;
    
    private String batchNo;
    
    private Long fromStockId;
    
    private Long toStockId;
    
    private String remark;
}

