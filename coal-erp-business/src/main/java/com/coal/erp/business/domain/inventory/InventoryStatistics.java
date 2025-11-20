package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存统计汇总表
 */
@Data
@TableName("inventory_statistics")
public class InventoryStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long statId;
    
    private Date statDate;
    
    private String statType;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private Integer totalMaterials;
    
    private BigDecimal totalQuantity;
    
    private BigDecimal totalValue;
    
    private Integer inboundCount;
    
    private BigDecimal inboundQuantity;
    
    private BigDecimal inboundAmount;
    
    private Integer outboundCount;
    
    private BigDecimal outboundQuantity;
    
    private BigDecimal outboundAmount;
    
    private Integer transferCount;
    
    private Integer adjustmentCount;
    
    private Integer stocktakingCount;
    
    private Integer warningCount;
    
    private BigDecimal turnoverRate;
    
    private Date createTime;
    
    private Date updateTime;
}

