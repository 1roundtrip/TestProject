package com.coal.erp.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 设备资产表
 */
@Data
@TableName("asset")
public class Asset implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String assetType;
    
    private String category;
    
    private String manufacturer;
    
    private String model;
    
    private String serialNumber;
    
    private Date purchaseDate;
    
    private BigDecimal purchasePrice;
    
    private String status;
    
    private String location;
    
    private Long deptId;
    
    private String isExplosionProof;
    
    private Date explosionProofExpireDate;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















