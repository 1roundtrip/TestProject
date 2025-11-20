package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购质检
 */
@Data
@TableName("purchase_quality_check")
public class PurchaseQualityCheck implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long checkId;
    
    private String checkNo;
    
    private Long receivingId;
    
    private String receivingNo;
    
    private Long orderId;
    
    private String orderNo;
    
    private Long supplierId;
    
    private String supplierName;
    
    private Date checkDate;
    
    private String checkType; // INCOMING-来料检验, PROCESS-过程检验, FINAL-最终检验
    
    private String checkMethod;
    
    private String checkStandard;
    
    private BigDecimal totalQuantity;
    
    private BigDecimal qualifiedQuantity;
    
    private BigDecimal unqualifiedQuantity;
    
    private BigDecimal qualifiedRate;
    
    private String checkResult; // PASSED-合格, FAILED-不合格, PARTIAL-部分合格
    
    private Long checkerId;
    
    private String checkerName;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private String status; // DRAFT-草稿, CHECKING-检验中, APPROVED-已审核, COMPLETED-已完成
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

