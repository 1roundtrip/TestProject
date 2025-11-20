package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购付款
 */
@Data
@TableName("purchase_payment")
public class PurchasePayment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long paymentId;
    
    private String paymentNo;
    
    private Long orderId;
    
    private String orderNo;
    
    private Long contractId;
    
    private String contractNo;
    
    private Long supplierId;
    
    private String supplierName;
    
    private String paymentType; // ADVANCE-预付款, PROGRESS-进度款, FINAL-尾款, OTHER-其他
    
    private Date paymentDate;
    
    private String paymentMethod; // TRANSFER-转账, CHECK-支票, CASH-现金, OTHER-其他
    
    private String currency;
    
    private BigDecimal paymentAmount;
    
    private BigDecimal orderAmount;
    
    private BigDecimal paidAmount;
    
    private BigDecimal balanceAmount;
    
    private String bankName;
    
    private String bankAccount;
    
    private String accountName;
    
    private String voucherNo;
    
    private String status; // DRAFT-草稿, SUBMITTED-已提交, APPROVED-已审批, PAID-已付款, REJECTED-已驳回, CANCELLED-已取消
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long payUserId;
    
    private String payUserName;
    
    private Date payTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

