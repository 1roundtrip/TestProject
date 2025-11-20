package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产入库管理
 */
@Data
@TableName("asset_storage")
public class AssetStorage implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long storageId;
    
    private String storageNo;
    
    private String storageType; // PURCHASE-采购入库, TRANSFER-调拨入库, REPAIR-维修入库, OTHER-其他
    
    private Date storageDate;
    
    private Long supplierId;
    
    private String supplierName;
    
    private BigDecimal totalAmount;
    
    private String warehouse;
    
    private String location;
    
    private String status; // DRAFT-草稿, CONFIRMED-已确认, CANCELLED-已取消
    
    private Long createUserId;
    
    private String createUserName;
    
    private Long auditUserId;
    
    private String auditUserName;
    
    private Date auditTime;
    
    private String remark;
    
    private Date createTime;
    
    private Date updateTime;
}

