package com.coal.erp.business.service.impl.hr;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.hr.HrDepartment;
import com.coal.erp.business.mapper.hr.HrDepartmentMapper;
import com.coal.erp.business.service.hr.HrDepartmentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

/**
 * 部门扩展服务实现
 */
@Service
public class HrDepartmentServiceImpl 
    extends ServiceImpl<HrDepartmentMapper, HrDepartment> 
    implements HrDepartmentService {

    @Override
    public R<?> updateDepartmentStats(Long deptId) {
        // TODO: 实现部门人数统计更新逻辑
        return R.success("部门统计更新成功");
    }

    @Override
    public R<?> getDepartmentTree() {
        // TODO: 实现部门树形结构查询
        return R.success("部门树获取成功");
    }

    @Override
    public boolean checkDeptCodeUnique(HrDepartment hrDepartment) {
        Long deptId = hrDepartment.getDeptId() == null ? -1L : hrDepartment.getDeptId();
        HrDepartment info = lambdaQuery()
            .eq(HrDepartment::getDeptCode, hrDepartment.getDeptCode())
            .one();
        return info == null || info.getDeptId().equals(deptId);
    }
}