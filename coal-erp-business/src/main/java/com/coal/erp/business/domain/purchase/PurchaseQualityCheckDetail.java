package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 采购质检明细
 */
@Data
@TableName("purchase_quality_check_detail")
public class PurchaseQualityCheckDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long checkId;
    
    private Long receivingDetailId;
    
    private String itemName;
    
    private String itemCode;
    
    private String specification;
    
    private BigDecimal checkQuantity;
    
    private BigDecimal qualifiedQuantity;
    
    private BigDecimal unqualifiedQuantity;
    
    private String checkItem;
    
    private String checkResult; // PASSED-合格, FAILED-不合格
    
    private String defectDescription;
    
    private String disposalMethod; // ACCEPT-接收, REJECT-拒收, REPAIR-返修, REPLACE-更换
    
    private String remark;
}

