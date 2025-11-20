package com.coal.erp.business.service.impl.hr;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.hr.HrEmployee;
import com.coal.erp.business.mapper.hr.HrEmployeeMapper;
import com.coal.erp.business.service.hr.HrEmployeeService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

/**
 * 员工服务实现
 */
@Service
public class HrEmployeeServiceImpl 
    extends ServiceImpl<HrEmployeeMapper, HrEmployee> 
    implements HrEmployeeService {

    @Override
    public R<?> createEmployee(HrEmployee employee) {
        if (!checkEmployeeCodeUnique(employee)) {
            return R.fail("员工工号已存在");
        }
        if (!checkIdCardUnique(employee)) {
            return R.fail("身份证号已存在");
        }
        
        // 设置默认状态
        if (employee.getWorkStatus() == null) {
            employee.setWorkStatus("PROBATION");
        }
        
        return R.success(save(employee));
    }

    @Override
    public R<?> updateEmployee(HrEmployee employee) {
        if (!checkEmployeeCodeUnique(employee)) {
            return R.fail("员工工号已存在");
        }
        if (!checkIdCardUnique(employee)) {
            return R.fail("身份证号已存在");
        }
        
        return R.success(updateById(employee));
    }

    @Override
    public R<?> deleteEmployee(Long employeeId) {
        // TODO: 检查员工是否有未完成的事项
        return R.success(removeById(employeeId));
    }

    @Override
    public R<?> getEmployeeFullInfo(Long employeeId) {
        HrEmployee employee = getById(employeeId);
        if (employee == null) {
            return R.fail("员工不存在");
        }
        
        // 设置掩码显示字段
        setMaskedFields(employee);
        
        // TODO: 加载关联信息（教育经历、工作经历等）
        return R.success(employee);
    }

    @Override
    public boolean checkEmployeeCodeUnique(HrEmployee employee) {
        Long employeeId = employee.getEmployeeId() == null ? -1L : employee.getEmployeeId();
        HrEmployee info = lambdaQuery()
            .eq(HrEmployee::getEmployeeCode, employee.getEmployeeCode())
            .one();
        return info == null || info.getEmployeeId().equals(employeeId);
    }

    @Override
    public boolean checkIdCardUnique(HrEmployee employee) {
        Long employeeId = employee.getEmployeeId() == null ? -1L : employee.getEmployeeId();
        HrEmployee info = lambdaQuery()
            .eq(HrEmployee::getIdCard, employee.getIdCard())
            .one();
        return info == null || info.getEmployeeId().equals(employeeId);
    }

    @Override
    public R<?> handleRegularization(Long employeeId) {
        HrEmployee employee = getById(employeeId);
        if (employee == null) {
            return R.fail("员工不存在");
        }
        
        if (!"PROBATION".equals(employee.getWorkStatus())) {
            return R.fail("员工状态不正确，无法转正");
        }
        
        employee.setWorkStatus("REGULAR");
        return R.success(updateById(employee));
    }

    @Override
    public R<?> handleResignation(Long employeeId, String reason) {
        HrEmployee employee = getById(employeeId);
        if (employee == null) {
            return R.fail("员工不存在");
        }
        
        employee.setWorkStatus("RESIGNED");
        return R.success(updateById(employee));
    }

    /**
     * 设置敏感字段的掩码显示
     */
    private void setMaskedFields(HrEmployee employee) {
        if (employee.getIdCard() != null) {
            employee.setIdCardMasked(com.coal.erp.common.utils.SecurityUtils.maskSensitiveInfo(employee.getIdCard()));
        }
        if (employee.getEmergencyPhone() != null) {
            employee.setEmergencyPhoneMasked(com.coal.erp.common.utils.SecurityUtils.maskSensitiveInfo(employee.getEmergencyPhone()));
        }
    }
}