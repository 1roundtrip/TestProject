package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 安全培训记录表
 */
@Data
@TableName("hr_safety_training")
public class HrSafetyTraining implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long trainingId;
    
    private Long employeeId;
    
    private String trainingType;
    
    private String trainingName;
    
    private Date trainingDate;
    
    private Integer trainingHours;
    
    private String trainingInstitution;
    
    private String trainerName;
    
    private String trainingContent;
    
    private String assessmentResult;
    
    private String certificateNo;
    
    private Date validUntil;
    
    private String attachmentPath;
    
    private Date createTime;
}