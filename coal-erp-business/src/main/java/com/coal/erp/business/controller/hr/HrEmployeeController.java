package com.coal.erp.business.controller.hr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.HrSecurityConfig;
import com.coal.erp.business.domain.hr.HrEmployee;
import com.coal.erp.business.service.hr.HrEmployeeService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 员工档案控制器
 */
@RestController
@RequestMapping("/hr/employee")
@HrSecurityConfig.RequiresEmployeePermission
public class HrEmployeeController {

    @Autowired
    private HrEmployeeService hrEmployeeService;

    /**
     * 分页查询员工档案
     */
    @GetMapping("/page")
    public R<Page<HrEmployee>> page(@RequestParam(defaultValue = "1") Long current,
                                   @RequestParam(defaultValue = "10") Long size,
                                   @RequestParam(required = false) String employeeCode,
                                   @RequestParam(required = false) String workStatus) {
        Page<HrEmployee> page = new Page<>(current, size);
        LambdaQueryWrapper<HrEmployee> wrapper = new LambdaQueryWrapper<>();
        
        if (employeeCode != null && !employeeCode.isEmpty()) {
            wrapper.like(HrEmployee::getEmployeeCode, employeeCode);
        }
        if (workStatus != null && !workStatus.isEmpty()) {
            wrapper.eq(HrEmployee::getWorkStatus, workStatus);
        }
        wrapper.orderByDesc(HrEmployee::getHireDate);

        return R.success(hrEmployeeService.page(page, wrapper));
    }

    /**
     * 获取员工完整信息
     */
    @GetMapping("/{employeeId}")
    public R<?> getEmployeeFullInfo(@PathVariable Long employeeId) {
        return hrEmployeeService.getEmployeeFullInfo(employeeId);
    }

    /**
     * 新增员工档案
     */
    @PostMapping
    @HrSecurityConfig.RequiresEmployeePermission("add")
    public R<?> createEmployee(@RequestBody HrEmployee employee) {
        return hrEmployeeService.createEmployee(employee);
    }

    /**
     * 修改员工档案
     */
    @PutMapping
    @HrSecurityConfig.RequiresEmployeePermission("edit")
    public R<?> updateEmployee(@RequestBody HrEmployee employee) {
        return hrEmployeeService.updateEmployee(employee);
    }

    /**
     * 删除员工档案
     */
    @DeleteMapping("/{employeeId}")
    @HrSecurityConfig.RequiresEmployeePermission("remove")
    public R<?> deleteEmployee(@PathVariable Long employeeId) {
        return hrEmployeeService.deleteEmployee(employeeId);
    }

    /**
     * 员工转正
     */
    @PostMapping("/{employeeId}/regularize")
    @HrSecurityConfig.RequiresEmployeePermission("regularize")
    public R<?> handleRegularization(@PathVariable Long employeeId) {
        return hrEmployeeService.handleRegularization(employeeId);
    }

    /**
     * 员工离职
     */
    @PostMapping("/{employeeId}/resign")
    @HrSecurityConfig.RequiresEmployeePermission("resign")
    public R<?> handleResignation(@PathVariable Long employeeId, 
                                @RequestParam String reason) {
        return hrEmployeeService.handleResignation(employeeId, reason);
    }

    /**
     * 获取在职状态枚举
     */
    @GetMapping("/statuses")
    public R<List<String>> getWorkStatuses() {
        List<String> statuses = Arrays.asList("PROBATION", "REGULAR", "RESIGNED", "RETIRED");
        return R.success(statuses);
    }

    /**
     * 获取用工类型枚举
     */
    @GetMapping("/employmentTypes")
    public R<List<String>> getEmploymentTypes() {
        List<String> types = Arrays.asList("FULL_TIME", "PART_TIME", "CONTRACT", "DISPATCH");
        return R.success(types);
    }
}