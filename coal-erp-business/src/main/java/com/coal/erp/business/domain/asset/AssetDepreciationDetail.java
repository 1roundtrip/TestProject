package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产折旧明细（按月记录）
 */
@Data
@TableName("asset_depreciation_detail")
public class AssetDepreciationDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long depreciationId;
    
    private Long assetId;
    
    private String depreciationMonth; // YYYY-MM
    
    private BigDecimal depreciationAmount; // 折旧金额
    
    private BigDecimal accumulatedAmount; // 累计折旧金额
    
    private BigDecimal netValue; // 净值
    
    private String status; // PENDING-待计提, CONFIRMED-已确认, CANCELLED-已取消
    
    private Date createTime;
    
    private Date confirmTime;
    
    private Long confirmUserId;
}

