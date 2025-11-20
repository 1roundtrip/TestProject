package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商产品目录
 */
@Data
@TableName("purchase_supplier_product")
public class PurchaseSupplierProduct implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long productId;
    
    private Long supplierId;
    
    private String productName;
    
    private String productCode;
    
    private String specification;
    
    private String brand;
    
    private String unit;
    
    private BigDecimal unitPrice;
    
    private String currency;
    
    private BigDecimal minOrderQuantity;
    
    private Integer deliveryDays;
    
    private Integer warrantyPeriod;
    
    private String status; // ACTIVE-启用, INACTIVE-停用
    
    private Date createTime;
    
    private Date updateTime;
}

