package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 劳动合同表
 */
@Data
@TableName("hr_contract")
public class HrContract implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long contractId;
    
    private Long employeeId;
    
    private String contractNo;
    
    private String contractType;
    
    private Date signDate;
    
    private Date startDate;
    
    private Date endDate;
    
    private Integer contractPeriod;
    
    private Integer trialPeriod;
    
    private String status;
    
    private BigDecimal salaryAmount;
    
    private String workLocation;
    
    private String jobPosition;
    
    private String remark;
    
    private String attachmentPath;
    
    private Date createTime;
    
    private Date updateTime;
}