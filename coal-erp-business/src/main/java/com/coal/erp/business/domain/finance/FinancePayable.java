package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 应付单据
 */
@Data
@TableName("finance_payable")
public class FinancePayable implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long payableId;
    
    private String payableNo;
    
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String sourceType;
    
    private String sourceNo;
    
    private BigDecimal amount;
    
    private BigDecimal paidAmount;
    
    private BigDecimal balanceAmount;
    
    private String currency;
    
    private BigDecimal exchangeRate;
    
    private Date issueDate;
    
    private Date dueDate;
    
    private String status;
    
    private String description;
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
}