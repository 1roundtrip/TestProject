package com.coal.erp.business.domain.inventory;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 仓库表
 */
@Data
@TableName("inventory_warehouse")
public class InventoryWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long warehouseId;
    
    private String warehouseCode;
    
    private String warehouseName;
    
    private String warehouseType;
    
    private String location;
    
    private Long managerId;
    
    private String managerName;
    
    private String contactPhone;
    
    private BigDecimal area;
    
    private BigDecimal capacity;
    
    private String capacityUnit;
    
    private String status;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

