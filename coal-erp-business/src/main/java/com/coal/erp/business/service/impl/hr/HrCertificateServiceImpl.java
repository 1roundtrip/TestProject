package com.coal.erp.business.service.impl.hr;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.hr.HrCertificate;
import com.coal.erp.business.mapper.hr.HrCertificateMapper;
import com.coal.erp.business.service.hr.HrCertificateService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 证照服务实现
 */
@Service
public class HrCertificateServiceImpl 
    extends ServiceImpl<HrCertificateMapper, HrCertificate> 
    implements HrCertificateService {

    @Override
    public R<?> createCertificate(HrCertificate certificate) {
        if (!checkCertificateUnique(certificate)) {
            return R.fail("证照已存在");
        }
        
        // 设置默认状态
        if (certificate.getStatus() == null) {
            certificate.setStatus("VALID");
        }
        
        return R.success(save(certificate));
    }

    @Override
    public R<?> updateCertificate(HrCertificate certificate) {
        if (!checkCertificateUnique(certificate)) {
            return R.fail("证照已存在");
        }
        
        return R.success(updateById(certificate));
    }

    @Override
    public boolean checkCertificateUnique(HrCertificate certificate) {
        Long certificateId = certificate.getCertificateId() == null ? -1L : certificate.getCertificateId();
        HrCertificate info = lambdaQuery()
            .eq(HrCertificate::getCertNumber, certificate.getCertNumber())
            .eq(HrCertificate::getCertType, certificate.getCertType())
            .one();
        return info == null || info.getCertificateId().equals(certificateId);
    }

    @Override
    public R<?> getEmployeeCertificates(Long employeeId) {
        List<HrCertificate> certificates = lambdaQuery()
            .eq(HrCertificate::getEmployeeId, employeeId)
            .orderByDesc(HrCertificate::getIssueDate)
            .list();
        return R.success(certificates);
    }

    @Override
    public R<?> getExpiringCertificates(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date targetDate = calendar.getTime();
        
        List<HrCertificate> certificates = lambdaQuery()
            .eq(HrCertificate::getStatus, "VALID")
            .le(HrCertificate::getExpireDate, targetDate)
            .orderByAsc(HrCertificate::getExpireDate)
            .list();
        
        return R.success(certificates);
    }

    @Override
    public R<?> getSpecialOperationCertificates() {
        List<HrCertificate> certificates = lambdaQuery()
            .isNotNull(HrCertificate::getSpecialOperationType)
            .eq(HrCertificate::getStatus, "VALID")
            .orderByAsc(HrCertificate::getExpireDate)
            .list();
        
        return R.success(certificates);
    }

    @Override
    public R<?> handleCertificateReview(Long certificateId, Date reviewDate, String result) {
        HrCertificate certificate = getById(certificateId);
        if (certificate == null) {
            return R.fail("证照不存在");
        }
        
        certificate.setReviewDate(reviewDate);
        if ("PASS".equals(result)) {
            // 复审通过，更新有效期
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reviewDate);
            calendar.add(Calendar.YEAR, 1); // 默认延长1年
            certificate.setExpireDate(calendar.getTime());
        } else {
            certificate.setStatus("INVALID");
        }
        
        return R.success(updateById(certificate));
    }
}