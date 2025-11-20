package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产盘点明细
 */
@Data
@TableName("asset_inventory_detail")
public class AssetInventoryDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long inventoryId;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private Integer bookQuantity; // 账面数量
    
    private Integer actualQuantity; // 实盘数量
    
    private Integer differenceQuantity; // 差异数量
    
    private String differenceType; // SURPLUS-盘盈, SHORTAGE-盘亏, NORMAL-正常
    
    private String differenceReason; // 差异原因
    
    private String handleStatus; // PENDING-待处理, PROCESSED-已处理
    
    private String handleRemark; // 处理备注
    
    private Date createTime;
}

