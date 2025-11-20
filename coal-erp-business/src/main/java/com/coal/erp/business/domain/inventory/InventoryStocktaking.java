package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存盘点表
 */
@Data
@TableName("inventory_stocktaking")
public class InventoryStocktaking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long stocktakingId;
    
    private String stocktakingNo;
    
    private String stocktakingType;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private Date stocktakingDate;
    
    private Date startTime;
    
    private Date endTime;
    
    private Integer totalItems;
    
    private Integer countedItems;
    
    private Integer surplusItems;
    
    private Integer shortageItems;
    
    private BigDecimal surplusAmount;
    
    private BigDecimal shortageAmount;
    
    private String status;
    
    private Long inventoryUserId;
    
    private String inventoryUserName;
    
    private Long confirmUserId;
    
    private String confirmUserName;
    
    private Date confirmTime;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

