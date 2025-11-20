package com.coal.erp.business.controller.hr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.HrSecurityConfig;
import com.coal.erp.business.domain.hr.HrSafetyTraining;
import com.coal.erp.business.service.hr.HrSafetyTrainingService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 安全培训控制器
 */
@RestController
@RequestMapping("/hr/training")
@HrSecurityConfig.RequiresTrainingPermission
public class HrSafetyTrainingController {

    @Autowired
    private HrSafetyTrainingService hrSafetyTrainingService;

    /**
     * 分页查询安全培训记录
     */
    @GetMapping("/page")
    public R<Page<HrSafetyTraining>> page(@RequestParam(defaultValue = "1") Long current,
                                         @RequestParam(defaultValue = "10") Long size,
                                         @RequestParam(required = false) String trainingType,
                                         @RequestParam(required = false) String trainingName) {
        Page<HrSafetyTraining> page = new Page<>(current, size);
        LambdaQueryWrapper<HrSafetyTraining> wrapper = new LambdaQueryWrapper<>();
        
        if (trainingType != null && !trainingType.isEmpty()) {
            wrapper.like(HrSafetyTraining::getTrainingType, trainingType);
        }
        if (trainingName != null && !trainingName.isEmpty()) {
            wrapper.like(HrSafetyTraining::getTrainingName, trainingName);
        }
        wrapper.orderByDesc(HrSafetyTraining::getTrainingDate);

        return R.success(hrSafetyTrainingService.page(page, wrapper));
    }

    /**
     * 获取员工安全培训记录
     */
    @GetMapping("/employee/{employeeId}")
    public R<?> getEmployeeTrainings(@PathVariable Long employeeId) {
        return hrSafetyTrainingService.getEmployeeTrainings(employeeId);
    }

    /**
     * 获取特种作业人员培训记录
     */
    @GetMapping("/special")
    public R<?> getSpecialOperatorTrainings() {
        return hrSafetyTrainingService.getSpecialOperatorTrainings();
    }

    /**
     * 获取即将到期的安全证书
     */
    @GetMapping("/expiring")
    public R<?> getExpiringSafetyCertificates(@RequestParam(defaultValue = "30") int days) {
        return hrSafetyTrainingService.getExpiringSafetyCertificates(days);
    }

    /**
     * 统计员工安全培训学时
     */
    @GetMapping("/hours/{employeeId}")
    public R<?> getEmployeeTrainingHours(@PathVariable Long employeeId,
                                       @RequestParam(required = false) String trainingType) {
        return hrSafetyTrainingService.getEmployeeTrainingHours(employeeId, trainingType);
    }

    /**
     * 新增安全培训记录
     */
    @PostMapping
    @HrSecurityConfig.RequiresTrainingPermission("add")
    public R<?> createSafetyTraining(@RequestBody HrSafetyTraining training) {
        return hrSafetyTrainingService.createSafetyTraining(training);
    }

    /**
     * 批量导入安全培训记录
     */
    @PostMapping("/import")
    @HrSecurityConfig.RequiresTrainingPermission("import")
    public R<?> importSafetyTrainings(@RequestBody List<HrSafetyTraining> trainings) {
        return hrSafetyTrainingService.importSafetyTrainings(trainings);
    }

    /**
     * 获取培训类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getTrainingTypes() {
        List<String> types = Arrays.asList(
            "SAFETY_INDUCTION",  // 安全入场培训
            "JOB_SPECIFIC",      // 岗位专项培训
            "SPECIAL_OPERATION", // 特种作业培训
            "EMERGENCY_DRILL",   // 应急演练
            "REFRESHER"          // 复训
        );
        return R.success(types);
    }
}