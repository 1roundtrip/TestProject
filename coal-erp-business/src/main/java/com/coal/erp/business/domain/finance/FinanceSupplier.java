package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商档案
 */
@Data
@TableName("finance_supplier")
public class FinanceSupplier implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String supplierType;
    
    private String evaluationLevel;
    
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