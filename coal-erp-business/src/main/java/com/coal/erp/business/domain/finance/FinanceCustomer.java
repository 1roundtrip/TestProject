package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户档案
 */
@Data
@TableName("finance_customer")
public class FinanceCustomer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long customerId;
    
    private String customerCode;
    
    private String customerName;
    
    private String customerType;
    
    private String creditLevel;
    
    private BigDecimal creditAmount;
    
    private String paymentTerms;
    
    private String contactPerson;
    
    private String contactPhone;
    
    private String address;
    
    private String taxNumber;
    
    private String bankAccount;
    
    private String bankName;
    
    private String status;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}