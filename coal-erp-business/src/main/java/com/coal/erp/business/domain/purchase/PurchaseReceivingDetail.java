package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购收货明细
 */
@Data
@TableName("purchase_receiving_detail")
public class PurchaseReceivingDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long receivingId;
    
    private Long orderDetailId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal orderQuantity;
    
    private BigDecimal receivedQuantity;
    
    private BigDecimal qualifiedQuantity;
    
    private BigDecimal unqualifiedQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalAmount;
    
    private String batchNo;
    
    private Date productionDate;
    
    private Date expiryDate;
    
    private String qualityStatus; // PENDING-待质检, PASSED-合格, FAILED-不合格
    
    private String storageStatus; // PENDING-待入库, STORED-已入库
    
    private String remark;
}

