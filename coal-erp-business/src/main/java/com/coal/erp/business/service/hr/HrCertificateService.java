package com.coal.erp.business.service.hr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.hr.HrCertificate;
import com.coal.erp.common.core.domain.R;
import java.util.Date;

/**
 * 证照服务接口
 */
public interface HrCertificateService extends IService<HrCertificate> {
    /**
     * 创建证照记录
     */
    R<?> createCertificate(HrCertificate certificate);
    
    /**
     * 更新证照记录
     */
    R<?> updateCertificate(HrCertificate certificate);
    
    /**
     * 校验证照唯一性
     */
    boolean checkCertificateUnique(HrCertificate certificate);
    
    /**
     * 获取员工证照列表
     */
    R<?> getEmployeeCertificates(Long employeeId);
    
    /**
     * 获取即将到期证照
     */
    R<?> getExpiringCertificates(int days);
    
    /**
     * 获取特种作业证照
     */
    R<?> getSpecialOperationCertificates();
    
    /**
     * 证照复审处理
     */
    R<?> handleCertificateReview(Long certificateId, Date reviewDate, String result);
}