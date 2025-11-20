package com.coal.erp.business.service.impl.hr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.hr.HrEmployee;
import com.coal.erp.business.domain.hr.HrSafetyTraining;
import com.coal.erp.business.mapper.hr.HrSafetyTrainingMapper;
import com.coal.erp.business.service.hr.HrEmployeeService;
import com.coal.erp.business.service.hr.HrSafetyTrainingService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 安全培训服务实现
 */
@Service
public class HrSafetyTrainingServiceImpl 
    extends ServiceImpl<HrSafetyTrainingMapper, HrSafetyTraining> 
    implements HrSafetyTrainingService {

    @Autowired
    private HrEmployeeService hrEmployeeService;

    @Override
    public R<?> createSafetyTraining(HrSafetyTraining training) {
        // 设置默认值
        if (training.getTrainingDate() == null) {
            training.setTrainingDate(new Date());
        }
        if (training.getValidUntil() == null && training.getTrainingHours() > 0) {
            // 默认有效期为1年
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(training.getTrainingDate());
            calendar.add(Calendar.YEAR, 1);
            training.setValidUntil(calendar.getTime());
        }
        
        return R.success(save(training));
    }

    @Override
    public R<?> getEmployeeTrainings(Long employeeId) {
        List<HrSafetyTraining> trainings = lambdaQuery()
            .eq(HrSafetyTraining::getEmployeeId, employeeId)
            .orderByDesc(HrSafetyTraining::getTrainingDate)
            .list();
        return R.success(trainings);
    }

    @Override
    public R<?> getSpecialOperatorTrainings() {
        // 先查询特种作业人员
        List<HrEmployee> specialOperators = hrEmployeeService.lambdaQuery()
            .eq(HrEmployee::getIsSpecialOperator, true)
            .list();
        
        List<Long> employeeIds = specialOperators.stream()
            .map(HrEmployee::getEmployeeId)
            .collect(Collectors.toList());
        
        if (employeeIds.isEmpty()) {
            return R.success(Collections.emptyList());
        }
        
        List<HrSafetyTraining> trainings = lambdaQuery()
            .in(HrSafetyTraining::getEmployeeId, employeeIds)
            .orderByDesc(HrSafetyTraining::getTrainingDate)
            .list();
        
        return R.success(trainings);
    }

    @Override
    public R<?> getExpiringSafetyCertificates(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date targetDate = calendar.getTime();
        
        List<HrSafetyTraining> certificates = lambdaQuery()
            .isNotNull(HrSafetyTraining::getValidUntil)
            .le(HrSafetyTraining::getValidUntil, targetDate)
            .orderByAsc(HrSafetyTraining::getValidUntil)
            .list();
        
        return R.success(certificates);
    }

    @Override
    public R<?> getEmployeeTrainingHours(Long employeeId, String trainingType) {
        LambdaQueryWrapper<HrSafetyTraining> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HrSafetyTraining::getEmployeeId, employeeId);
        
        if (trainingType != null && !trainingType.isEmpty()) {
            wrapper.eq(HrSafetyTraining::getTrainingType, trainingType);
        }
        
        List<HrSafetyTraining> trainings = list(wrapper);
        int totalHours = trainings.stream()
            .mapToInt(HrSafetyTraining::getTrainingHours)
            .sum();
        
        return R.success(totalHours);
    }

    @Override
    public R<?> importSafetyTrainings(List<HrSafetyTraining> trainings) {
        if (trainings == null || trainings.isEmpty()) {
            return R.fail("导入数据为空");
        }
        
        boolean success = saveBatch(trainings);
        return success ? R.success("导入成功") : R.fail("导入失败");
    }
}