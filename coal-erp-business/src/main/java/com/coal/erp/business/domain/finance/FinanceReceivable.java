package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 应收单据
 */
@Data
@TableName("finance_receivable")
public class FinanceReceivable implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long receivableId;
    
    private String receivableNo;
    
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    private String sourceType;
    
    private String sourceNo;
    
    private BigDecimal amount;
    
    private BigDecimal receivedAmount;
    
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