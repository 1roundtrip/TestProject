package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购退货
 */
@Data
@TableName("purchase_return")
public class PurchaseReturn implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long returnId;
    
    private String returnNo;
    
    private Long receivingId;
    
    private String receivingNo;
    
    private Long orderId;
    
    private String orderNo;
    
    private Long supplierId;
    
    private String supplierName;
    
    private Date returnDate;
    
    private String returnType; // QUALITY-质量问题, QUANTITY-数量错误, SPECIFICATION-规格不符, OTHER-其他
    
    private String returnReason;
    
    private BigDecimal totalAmount;
    
    private String logisticsCompany;
    
    private String logisticsNo;
    
    private String status; // DRAFT-草稿, SUBMITTED-已提交, APPROVED-已审批, CONFIRMED-已确认, RETURNING-退货中, RETURNED-已退货, REJECTED-已驳回, CANCELLED-已取消
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long returnUserId;
    
    private String returnUserName;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

