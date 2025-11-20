package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购申请明细
 */
@Data
@TableName("purchase_requisition_detail")
public class PurchaseRequisitionDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long requisitionId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private String brand;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal estimatedPrice;
    
    private BigDecimal estimatedAmount;
    
    private Date requiredDate;
    
    private String purpose;
    
    private String remark;
}

