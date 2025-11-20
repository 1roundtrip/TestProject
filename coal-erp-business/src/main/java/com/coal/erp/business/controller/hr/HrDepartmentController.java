package com.coal.erp.business.controller.hr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.HrSecurityConfig;
import com.coal.erp.business.domain.hr.HrDepartment;
import com.coal.erp.business.service.hr.HrDepartmentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 部门扩展控制器
 */
@RestController
@RequestMapping("/hr/department")
@HrSecurityConfig.RequiresHrPermission("department")
public class HrDepartmentController {

    @Autowired
    private HrDepartmentService hrDepartmentService;

    /**
     * 分页查询部门扩展信息
     */
    @GetMapping("/page")
    public R<Page<HrDepartment>> page(@RequestParam(defaultValue = "1") Long current,
                                     @RequestParam(defaultValue = "10") Long size,
                                     @RequestParam(required = false) String deptCode,
                                     @RequestParam(required = false) String deptType) {
        Page<HrDepartment> page = new Page<>(current, size);
        LambdaQueryWrapper<HrDepartment> wrapper = new LambdaQueryWrapper<>();
        
        if (deptCode != null && !deptCode.isEmpty()) {
            wrapper.like(HrDepartment::getDeptCode, deptCode);
        }
        if (deptType != null && !deptType.isEmpty()) {
            wrapper.eq(HrDepartment::getDeptType, deptType);
        }
        wrapper.orderByAsc(HrDepartment::getDeptCode);

        return R.success(hrDepartmentService.page(page, wrapper));
    }

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    public R<List<HrDepartment>> list() {
        return R.success(hrDepartmentService.list());
    }

    /**
     * 根据ID获取部门详情
     */
    @GetMapping("/{deptId}")
    public R<HrDepartment> getById(@PathVariable Long deptId) {
        return R.success(hrDepartmentService.getById(deptId));
    }

    /**
     * 新增部门扩展信息
     */
    @PostMapping
    @HrSecurityConfig.RequiresHrPermission("department:add")
    public R<?> add(@RequestBody HrDepartment hrDepartment) {
        if (!hrDepartmentService.checkDeptCodeUnique(hrDepartment)) {
            return R.fail("部门编码已存在");
        }
        return R.success(hrDepartmentService.save(hrDepartment));
    }

    /**
     * 修改部门扩展信息
     */
    @PutMapping
    @HrSecurityConfig.RequiresHrPermission("department:edit")
    public R<?> update(@RequestBody HrDepartment hrDepartment) {
        if (!hrDepartmentService.checkDeptCodeUnique(hrDepartment)) {
            return R.fail("部门编码已存在");
        }
        return R.success(hrDepartmentService.updateById(hrDepartment));
    }

    /**
     * 删除部门扩展信息
     */
    @DeleteMapping("/{deptId}")
    @HrSecurityConfig.RequiresHrPermission("department:remove")
    public R<?> delete(@PathVariable Long deptId) {
        return R.success(hrDepartmentService.removeById(deptId));
    }

    /**
     * 更新部门统计信息
     */
    @PostMapping("/{deptId}/stats")
    @HrSecurityConfig.RequiresHrPermission("department:stats")
    public R<?> updateStats(@PathVariable Long deptId) {
        return hrDepartmentService.updateDepartmentStats(deptId);
    }

    /**
     * 获取部门类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getDeptTypes() {
        List<String> types = Arrays.asList("ADMIN", "PRODUCTION", "SAFETY", "LOGISTICS");
        return R.success(types);
    }
}