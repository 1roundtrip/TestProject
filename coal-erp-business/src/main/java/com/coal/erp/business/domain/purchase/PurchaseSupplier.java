package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商
 */
@Data
@TableName("purchase_supplier")
public class PurchaseSupplier implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long supplierId;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String supplierType; // MAIN-主要供应商, AUXILIARY-辅助供应商, STRATEGIC-战略供应商
    
    private String creditLevel; // AAA-优秀, AA-良好, A-一般, B-较差
    
    private Integer cooperationYears;
    
    private String businessLicense;
    
    private String taxNumber;
    
    private String legalPerson;
    
    private BigDecimal registeredCapital;
    
    private String contactPerson;
    
    private String contactPhone;
    
    private String contactEmail;
    
    private String address;
    
    private String bankName;
    
    private String bankAccount;
    
    private String accountName;
    
    private String paymentTerms;
    
    private String deliveryTerms;
    
    private BigDecimal qualityRating;
    
    private BigDecimal serviceRating;
    
    private BigDecimal priceRating;
    
    private BigDecimal totalRating;
    
    private String status; // ACTIVE-启用, INACTIVE-停用, BLACKLIST-黑名单
    
    private String blacklistReason;
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

