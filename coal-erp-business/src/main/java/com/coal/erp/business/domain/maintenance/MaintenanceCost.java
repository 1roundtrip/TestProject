package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修成本表
 */
@Data
@TableName("maintenance_cost")
public class MaintenanceCost implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long costId;
    
    private Long workOrderId;
    
    private String workOrderNo;
    
    private String costType;
    
    private String costCategory;
    
    private String costItem;
    
    private BigDecimal quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal amount;
    
    private Long supplierId;
    
    private String supplierName;
    
    private String invoiceNo;
    
    private Date costDate;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

