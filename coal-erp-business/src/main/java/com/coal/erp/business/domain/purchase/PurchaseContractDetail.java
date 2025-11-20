package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购合同明细
 */
@Data
@TableName("purchase_contract_detail")
public class PurchaseContractDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long contractId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalAmount;
    
    private Date deliveryDate;
    
    private String remark;
}

