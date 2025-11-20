package com.coal.erp.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存表
 */
@Data
@TableName("inventory")
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long inventoryId;
    
    private String materialCode;
    
    private String materialName;
    
    private String materialType;
    
    private String unit;
    
    private BigDecimal quantity;
    
    private BigDecimal minStock;
    
    private BigDecimal maxStock;
    
    private String warehouse;
    
    private String location;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















