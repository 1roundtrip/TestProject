package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购统计汇总
 */
@Data
@TableName("purchase_statistics")
public class PurchaseStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long statId;
    
    private Date statDate;
    
    private String statType; // DAILY-日报, MONTHLY-月报, QUARTERLY-季报, YEARLY-年报
    
    private Long deptId;
    
    private String deptName;
    
    private Long supplierId;
    
    private String supplierName;
    
    private Integer orderCount;
    
    private BigDecimal orderAmount;
    
    private BigDecimal receivedAmount;
    
    private BigDecimal paidAmount;
    
    private BigDecimal returnAmount;
    
    private BigDecimal qualityPassRate;
    
    private BigDecimal onTimeDeliveryRate;
    
    private Date createTime;
}

