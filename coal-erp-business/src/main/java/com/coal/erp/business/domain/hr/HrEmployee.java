package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.coal.erp.business.utils.EncryptedField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工主表
 */
@Data
@TableName("hr_employee")
public class HrEmployee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long employeeId;
    
    private Long userId;
    
    private String employeeCode;
    
    @EncryptedField
    private String idCard;
    
    private String gender;
    
    private Date birthDate;
    
    private String nationality;
    
    private String maritalStatus;
    
    private String politicalStatus;
    
    private String nativePlace;
    
    private String householdType;
    
    @EncryptedField
    private String currentAddress;
    
    @EncryptedField
    private String emergencyContact;
    
    @EncryptedField
    private String emergencyPhone;
    
    private Long positionId;
    
    private Date hireDate;
    
    private String workStatus;
    
    private String employmentType;
    
    private String workShift;
    
    private Boolean isUndergroundWorker;
    
    private Boolean isSpecialOperator;
    
    private Date createTime;
    
    private Date updateTime;
    
    /**
     * 身份证号掩码显示（不存储）
     */
    @TableField(exist = false)
    private String idCardMasked;
    
    /**
     * 紧急联系电话掩码显示（不存储）
     */
    @TableField(exist = false)
    private String emergencyPhoneMasked;
}