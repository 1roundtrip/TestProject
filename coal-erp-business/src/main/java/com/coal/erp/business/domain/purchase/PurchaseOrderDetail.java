package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单明细
 */
@Data
@TableName("purchase_order_detail")
public class PurchaseOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long orderId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private String brand;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal taxRate;
    
    private BigDecimal amount;
    
    private BigDecimal taxAmount;
    
    private BigDecimal amountWithTax;
    
    private BigDecimal receivedQuantity;
    
    private Date requiredDate;
    
    private String remark;
}

