package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购退货明细
 */
@Data
@TableName("purchase_return_detail")
public class PurchaseReturnDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long returnId;
    
    private Long receivingDetailId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal returnQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalAmount;
    
    private String returnReason;
    
    private String batchNo;
    
    private String remark;
}

