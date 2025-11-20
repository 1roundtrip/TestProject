package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购计划
 */
@Data
@TableName("purchase_plan")
public class PurchasePlan implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long planId;
    
    private String planNo;
    
    private String planName;
    
    private Integer planYear;
    
    private Integer planQuarter;
    
    private Integer planMonth;
    
    private Long deptId;
    
    private String deptName;
    
    private BigDecimal budgetAmount;
    
    private BigDecimal totalAmount;
    
    private String status; // DRAFT-草稿, SUBMITTED-已提交, APPROVED-已审批, REJECTED-已驳回, EXECUTING-执行中, COMPLETED-已完成, CANCELLED-已取消
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private String approveRemark;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

