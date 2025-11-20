package com.coal.erp.business.service.hr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.hr.HrEmployee;
import com.coal.erp.common.core.domain.R;

/**
 * 员工服务接口
 */
public interface HrEmployeeService extends IService<HrEmployee> {
    /**
     * 创建员工档案
     */
    R<?> createEmployee(HrEmployee employee);
    
    /**
     * 更新员工档案
     */
    R<?> updateEmployee(HrEmployee employee);
    
    /**
     * 删除员工档案
     */
    R<?> deleteEmployee(Long employeeId);
    
    /**
     * 获取员工完整信息
     */
    R<?> getEmployeeFullInfo(Long employeeId);
    
    /**
     * 校验员工工号唯一性
     */
    boolean checkEmployeeCodeUnique(HrEmployee employee);
    
    /**
     * 校验身份证号唯一性
     */
    boolean checkIdCardUnique(HrEmployee employee);
    
    /**
     * 员工转正处理
     */
    R<?> handleRegularization(Long employeeId);
    
    /**
     * 员工离职处理
     */
    R<?> handleResignation(Long employeeId, String reason);
}