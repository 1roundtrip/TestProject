package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教育经历表
 */
@Data
@TableName("hr_education")
public class HrEducation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long educationId;
    
    private Long employeeId;
    
    private String educationLevel;
    
    private String schoolName;
    
    private String major;
    
    private Date startDate;
    
    private Date endDate;
    
    private String degree;
    
    private Boolean isFullTime;
    
    private String graduationCertNo;
    
    private String degreeCertNo;
    
    private Date createTime;
}