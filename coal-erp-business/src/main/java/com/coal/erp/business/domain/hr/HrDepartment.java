package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门扩展表
 */
@Data
@TableName("hr_department")
public class HrDepartment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long deptId;
    
    private String deptCode;
    
    private String deptType;
    
    private Date establishDate;
    
    private Integer budgetCount;
    
    private Integer actualCount;
    
    private String costCenter;
    
    private Boolean isProduction;
    
    private Boolean isSafetyCritical;
}