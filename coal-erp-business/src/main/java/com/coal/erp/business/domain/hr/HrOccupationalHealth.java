package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 职业病健康档案表
 */
@Data
@TableName("hr_occupational_health")
public class HrOccupationalHealth implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long healthId;
    
    private Long employeeId;
    
    private Date checkDate;
    
    private String checkType;
    
    private String checkInstitution;
    
    private String checkResult;
    
    private String diagnosisResult;
    
    private String suggestion;
    
    private Date nextCheckDate;
    
    private String attachmentPath;
    
    private Date createTime;
}