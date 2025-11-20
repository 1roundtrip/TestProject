package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 维修质量检查表
 */
@Data
@TableName("maintenance_quality_check")
public class MaintenanceQualityCheck implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long checkId;
    
    private String checkNo;
    
    private Long workOrderId;
    
    private String workOrderNo;
    
    private String checkType;
    
    private Date checkDate;
    
    private Long checkerId;
    
    private String checkerName;
    
    private String checkItems;
    
    private String checkResult;
    
    private BigDecimal qualityScore;
    
    private String defectDescription;
    
    private String rectificationRequired;
    
    private Date rectificationDeadline;
    
    private String rectificationStatus;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

