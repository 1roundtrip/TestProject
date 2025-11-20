package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产转移调拨管理
 */
@Data
@TableName("asset_transfer")
public class AssetTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long transferId;
    
    private String transferNo;
    
    private Date transferDate;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private Long fromDeptId;
    
    private String fromDeptName;
    
    private String fromLocation;
    
    private Long toDeptId;
    
    private String toDeptName;
    
    private String toLocation;
    
    private String transferReason;
    
    private String status; // PENDING-待转移, TRANSFERRED-已转移, CANCELLED-已取消
    
    private Long createUserId;
    
    private String createUserName;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private Long transferUserId;
    
    private String transferUserName;
    
    private Date transferTime;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

