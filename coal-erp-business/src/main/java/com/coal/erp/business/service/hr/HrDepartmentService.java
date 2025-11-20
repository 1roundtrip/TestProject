package com.coal.erp.business.service.hr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.hr.HrDepartment;
import com.coal.erp.common.core.domain.R;

/**
 * 部门扩展服务接口
 */
public interface HrDepartmentService extends IService<HrDepartment> {
    /**
     * 更新部门编制和人数统计
     */
    R<?> updateDepartmentStats(Long deptId);
    
    /**
     * 获取部门树形结构
     */
    R<?> getDepartmentTree();
    
    /**
     * 校验部门编码唯一性
     */
    boolean checkDeptCodeUnique(HrDepartment hrDepartment);
}