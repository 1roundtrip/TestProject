package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 岗位表
 */
@Data
@TableName("hr_position")
public class HrPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long positionId;
    
    private String positionCode;
    
    private String positionName;
    
    private Long deptId;
    
    private String positionLevel;
    
    private String positionCategory;
    
    private Boolean isSpecialOperation;
    
    private String specialOperationType;
    
    private String jobDescription;
    
    private String requirements;
    
    private BigDecimal minSalary;
    
    private BigDecimal maxSalary;
    
    private Boolean status;
    
    private Date createTime;
    
    private Date updateTime;
}
