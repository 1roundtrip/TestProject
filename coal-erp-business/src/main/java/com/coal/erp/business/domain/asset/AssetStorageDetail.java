package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产入库明细
 */
@Data
@TableName("asset_storage_detail")
public class AssetStorageDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long storageId;
    
    private Long assetId; // 已存在资产ID
    
    private String assetCode;
    
    private String assetName;
    
    private String assetType;
    
    private String category;
    
    private String manufacturer;
    
    private String model;
    
    private String serialNumber;
    
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
    
    private Date purchaseDate;
    
    private Integer warrantyPeriod; // 保修期（月）
    
    private String remark;
    
    private Date createTime;
}

