package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商评价记录
 */
@Data
@TableName("purchase_supplier_evaluation")
public class PurchaseSupplierEvaluation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long evaluationId;
    
    private Long supplierId;
    
    private Long orderId;
    
    private String orderNo;
    
    private Date evaluationDate;
    
    private BigDecimal qualityScore;
    
    private BigDecimal deliveryScore;
    
    private BigDecimal serviceScore;
    
    private BigDecimal priceScore;
    
    private BigDecimal totalScore;
    
    private String evaluationContent;
    
    private Long evaluatorId;
    
    private String evaluatorName;
    
    private Date createTime;
}

