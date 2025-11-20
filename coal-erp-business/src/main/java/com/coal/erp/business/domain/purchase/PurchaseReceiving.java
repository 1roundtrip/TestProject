package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购收货
 */
@Data
@TableName("purchase_receiving")
public class PurchaseReceiving implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long receivingId;
    
    private String receivingNo;
    
    private Long orderId;
    
    private String orderNo;
    
    private Long contractId;
    
    private String contractNo;
    
    private Long supplierId;
    
    private String supplierName;
    
    private Date receivingDate;
    
    private String warehouse;
    
    private String location;
    
    private String deliveryNo;
    
    private String logisticsCompany;
    
    private String logisticsNo;
    
    private BigDecimal totalAmount;
    
    private String status; // DRAFT-草稿, CONFIRMED-已确认, QUALITY_CHECKING-质检中, QUALITY_PASSED-质检通过, QUALITY_FAILED-质检不合格, STORED-已入库, CANCELLED-已取消
    
    private Long receiverId;
    
    private String receiverName;
    
    private Long warehouseKeeperId;
    
    private String warehouseKeeperName;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

