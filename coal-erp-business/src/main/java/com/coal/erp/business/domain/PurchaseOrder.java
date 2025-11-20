package com.coal.erp.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单表（扩展版）
 */
@Data
@TableName("purchase_order")
public class PurchaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long orderId;
    
    private String orderNo;
    
    private Long requisitionId;
    
    private String requisitionNo;
    
    private Long supplierId;
    
    private String supplier;
    
    private String supplierName;
    
    private String supplierCode;
    
    private String orderType;
    
    private Date orderDate;
    
    private Date deliveryDate;
    
    private String deliveryAddress;
    
    private String deliveryMethod;
    
    private String paymentTerms;
    
    private String currency;
    
    private BigDecimal totalAmount;
    
    private BigDecimal taxAmount;
    
    private BigDecimal totalAmountWithTax;
    
    private String status;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long buyerId;
    
    private String buyerName;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}











