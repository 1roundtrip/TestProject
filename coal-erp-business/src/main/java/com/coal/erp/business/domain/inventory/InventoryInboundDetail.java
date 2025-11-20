package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 入库明细表
 */
@Data
@TableName("inventory_inbound_detail")
public class InventoryInboundDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long inboundId;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal receivedQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal amount;
    
    private String batchNo;
    
    private Date productionDate;
    
    private Date expiryDate;
    
    private Long locationId;
    
    private String locationCode;
    
    private String remark;
}

