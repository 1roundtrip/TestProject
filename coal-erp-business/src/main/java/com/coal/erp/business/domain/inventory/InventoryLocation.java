package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库位表
 */
@Data
@TableName("inventory_location")
public class InventoryLocation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long locationId;
    
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private String locationCode;
    
    private String locationName;
    
    private String locationType;
    
    private String zone;
    
    private String aisle;
    
    private String shelf;
    
    private String level;
    
    private String position;
    
    private BigDecimal capacity;
    
    private String capacityUnit;
    
    private String status;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

