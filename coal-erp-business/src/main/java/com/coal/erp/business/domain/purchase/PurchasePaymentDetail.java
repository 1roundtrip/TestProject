package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购付款明细
 */
@Data
@TableName("purchase_payment_detail")
public class PurchasePaymentDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long paymentId;
    
    private Long orderId;
    
    private String orderNo;
    
    private Long receivingId;
    
    private String receivingNo;
    
    private String itemName;
    
    private BigDecimal paymentAmount;
    
    private String remark;
}

