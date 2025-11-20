package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产盘点管理
 */
@Data
@TableName("asset_inventory")
public class AssetInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long inventoryId;
    
    private String inventoryNo;
    
    private String inventoryType; // FULL-全面盘点, PARTIAL-部分盘点, SPOT-抽查盘点
    
    private Date inventoryDate;
    
    private String warehouse;
    
    private Long deptId;
    
    private String deptName;
    
    private String status; // DRAFT-草稿, IN_PROGRESS-盘点中, COMPLETED-已完成, CONFIRMED-已确认
    
    private Integer totalCount; // 应盘数量
    
    private Integer actualCount; // 实盘数量
    
    private Integer surplusCount; // 盘盈数量
    
    private Integer shortageCount; // 盘亏数量
    
    private Long createUserId;
    
    private String createUserName;
    
    private Long inventoryUserId;
    
    private String inventoryUserName;
    
    private Long confirmUserId;
    
    private String confirmUserName;
    
    private Date confirmTime;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

