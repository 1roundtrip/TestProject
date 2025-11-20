package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 家庭关系表
 */
@Data
@TableName("hr_family_relation")
public class HrFamilyRelation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long relationId;
    
    private Long employeeId;
    
    private String relationType;
    
    private String fullName;
    
    private String idCard;
    
    private String phone;
    
    private String workUnit;
    
    private String position;
    
    private Boolean isEmergencyContact;
    
    private Date createTime;
}