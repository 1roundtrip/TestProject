package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产折旧管理
 */
@Data
@TableName("asset_depreciation")
public class AssetDepreciation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long depreciationId;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String depreciationMethod; // STRAIGHT_LINE-直线法, ACCELERATED-加速折旧法
    
    private BigDecimal originalValue; // 原值
    
    private BigDecimal residualValue; // 残值
    
    private Integer usefulLife; // 使用年限（月）
    
    private BigDecimal depreciationRate; // 折旧率（%）
    
    private BigDecimal monthlyDepreciation; // 月折旧额
    
    private BigDecimal accumulatedDepreciation; // 累计折旧
    
    private BigDecimal netValue; // 净值
    
    private Date startDate; // 开始折旧日期
    
    private Date lastDepreciationDate; // 最后折旧日期
    
    private String status; // ACTIVE-折旧中, STOPPED-已停用, COMPLETED-已提完
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

