package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收付款单
 */
@Data
@TableName("finance_payment")
public class FinancePayment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long paymentId;
    
    private String paymentNo;
    
    private String paymentType;
    
    private Long customerId;
    
    private Long supplierId;
    
    private BigDecimal amount;
    
    private String currency;
    
    private BigDecimal exchangeRate;
    
    private Date paymentDate;
    
    private String paymentMethod;
    
    private String bankAccount;
    
    private String status;
    
    private String description;
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
}