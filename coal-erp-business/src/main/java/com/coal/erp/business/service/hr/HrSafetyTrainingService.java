package com.coal.erp.business.service.hr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.hr.HrSafetyTraining;
import com.coal.erp.common.core.domain.R;
import java.util.List;

/**
 * 安全培训服务接口
 */
public interface HrSafetyTrainingService extends IService<HrSafetyTraining> {
    /**
     * 创建安全培训记录
     */
    R<?> createSafetyTraining(HrSafetyTraining training);
    
    /**
     * 获取员工安全培训记录
     */
    R<?> getEmployeeTrainings(Long employeeId);
    
    /**
     * 获取特种作业人员培训记录
     */
    R<?> getSpecialOperatorTrainings();
    
    /**
     * 获取即将到期的安全证书
     */
    R<?> getExpiringSafetyCertificates(int days);
    
    /**
     * 统计员工安全培训学时
     */
    R<?> getEmployeeTrainingHours(Long employeeId, String trainingType);
    
    /**
     * 批量导入安全培训记录
     */
    R<?> importSafetyTrainings(List<HrSafetyTraining> trainings);
}