package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工作经历表
 */
@Data
@TableName("hr_work_experience")
public class HrWorkExperience implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long experienceId;
    
    private Long employeeId;
    
    private String companyName;
    
    private String position;
    
    private Date startDate;
    
    private Date endDate;
    
    private String jobDescription;
    
    private String referenceName;
    
    private String referencePhone;
    
    private Date createTime;
}