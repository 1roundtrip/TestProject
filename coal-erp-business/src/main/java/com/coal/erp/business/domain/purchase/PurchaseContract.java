package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购合同
 */
@Data
@TableName("purchase_contract")
public class PurchaseContract implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long contractId;
    
    private String contractNo;
    
    private String contractName;
    
    private Long orderId;
    
    private String orderNo;
    
    private Long supplierId;
    
    private String supplierName;
    
    private String contractType; // FRAMEWORK-框架合同, SPECIFIC-具体合同
    
    private Date contractDate;
    
    private Date startDate;
    
    private Date endDate;
    
    private BigDecimal totalAmount;
    
    private String currency;
    
    private String paymentMethod;
    
    private String paymentSchedule; // JSON格式
    
    private String deliveryTerms;
    
    private String qualityTerms;
    
    private String warrantyTerms;
    
    private String penaltyTerms;
    
    private String contractFile;
    
    private String status; // DRAFT-草稿, SUBMITTED-已提交, APPROVED-已审批, SIGNED-已签订, EXECUTING-执行中, COMPLETED-已完成, TERMINATED-已终止
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long signUserId;
    
    private String signUserName;
    
    private Date signTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

