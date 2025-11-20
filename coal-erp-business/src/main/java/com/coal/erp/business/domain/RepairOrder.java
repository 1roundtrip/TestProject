package com.coal.erp.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修工单表
 */
@Data
@TableName("repair_order")
public class RepairOrder implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long repairId;
    
    private String repairNo;
    
    private Long assetId;
    
    private String faultDescription;
    
    private String repairType;
    
    private BigDecimal repairCost;
    
    private String status;
    
    private Long repairUserId;
    
    private Date repairStartTime;
    
    private Date repairEndTime;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















