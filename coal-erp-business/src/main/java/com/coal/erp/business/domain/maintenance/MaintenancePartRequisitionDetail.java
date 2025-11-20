package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 维修备件领用明细表
 */
@Data
@TableName("maintenance_part_requisition_detail")
public class MaintenancePartRequisitionDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long requisitionId;
    
    private Long materialId;
    
    private String materialCode;
    
    private String materialName;
    
    private String specification;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal issuedQuantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal amount;
    
    private String remark;
}

