package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警统计表
 */
@Data
@TableName("warning_statistics")
public class WarningStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long statId;
    
    private Date statDate;
    
    private String statType;
    
    private String warningType;
    
    private Long warningLevelId;
    
    private Integer totalCount;
    
    private Integer pendingCount;
    
    private Integer processingCount;
    
    private Integer resolvedCount;
    
    private Integer ignoredCount;
    
    private Integer avgResolveTime;
    
    private Date createTime;
    
    private Date updateTime;
}

