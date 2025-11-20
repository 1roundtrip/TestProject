package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购计划明细
 */
@Data
@TableName("purchase_plan_detail")
public class PurchasePlanDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long planId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal estimatedPrice;
    
    private BigDecimal estimatedAmount;
    
    private String purpose;
    
    private Date requiredDate;
    
    private String priority; // HIGH-高, MEDIUM-中, LOW-低
    
    private String remark;
}

