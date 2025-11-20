package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产报废管理
 */
@Data
@TableName("asset_scrap")
public class AssetScrap implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long scrapId;
    
    private String scrapNo;
    
    private Date scrapDate;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private String scrapReason; // 报废原因
    
    private String scrapType; // NATURAL-自然报废, DAMAGE-损坏报废, REPLACE-更新换代, OTHER-其他
    
    private BigDecimal originalValue; // 原值
    
    private BigDecimal netValue; // 净值
    
    private BigDecimal scrapValue; // 残值
    
    private String status; // PENDING-待审批, APPROVED-已审批, REJECTED-已驳回, COMPLETED-已完成
    
    private Long applyUserId;
    
    private String applyUserName;
    
    private Date applyTime;
    
    private Long approveUserId;
    
    private String approveUserName;
    
    private Date approveTime;
    
    private String approveRemark; // 审批意见
    
    private Long handleUserId;
    
    private String handleUserName;
    
    private Date handleTime;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

