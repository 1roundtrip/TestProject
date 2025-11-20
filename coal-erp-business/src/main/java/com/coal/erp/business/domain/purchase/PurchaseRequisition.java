package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购申请
 */
@Data
@TableName("purchase_requisition")
public class PurchaseRequisition implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long requisitionId;
    
    private String requisitionNo;
    
    private Long planId;
    
    private String planNo;
    
    private String requisitionName;
    
    private Long deptId;
    
    private String deptName;
    
    private Long applicantId;
    
    private String applicantName;
    
    private BigDecimal totalAmount;
    
    private String urgentLevel; // URGENT-紧急, NORMAL-正常, LOW-不急
    
    private String purpose;
    
    private String status; // DRAFT-草稿, SUBMITTED-已提交, APPROVING-审批中, APPROVED-已审批, REJECTED-已驳回, ORDERED-已下单, CANCELLED-已取消
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private String approveRemark;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

