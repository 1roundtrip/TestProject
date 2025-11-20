package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 员工异动记录表
 */
@Data
@TableName("hr_employee_change")
public class HrEmployeeChange implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long changeId;
    
    private Long employeeId;
    
    private String changeType;
    
    private Date changeDate;
    
    private String oldValue;
    
    private String newValue;
    
    private String reason;
    
    private Long approverId;
    
    private String approvalStatus;
    
    private Date approvalTime;
    
    private String remark;
    
    private Date createTime;
}