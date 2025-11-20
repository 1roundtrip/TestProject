package com.coal.erp.business.controller.hr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.HrSecurityConfig;
import com.coal.erp.business.domain.hr.HrCertificate;
import com.coal.erp.business.service.hr.HrCertificateService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 * 证照管理控制器
 */
@RestController
@RequestMapping("/hr/certificate")
@HrSecurityConfig.RequiresCertificatePermission
public class HrCertificateController {

    @Autowired
    private HrCertificateService hrCertificateService;

    /**
     * 分页查询证照
     */
    @GetMapping("/page")
    public R<Page<HrCertificate>> page(@RequestParam(defaultValue = "1") Long current,
                                      @RequestParam(defaultValue = "10") Long size,
                                      @RequestParam(required = false) String certNumber,
                                      @RequestParam(required = false) String certType,
                                      @RequestParam(required = false) String status) {
        Page<HrCertificate> page = new Page<>(current, size);
        LambdaQueryWrapper<HrCertificate> wrapper = new LambdaQueryWrapper<>();
        
        if (certNumber != null && !certNumber.isEmpty()) {
            wrapper.like(HrCertificate::getCertNumber, certNumber);
        }
        if (certType != null && !certType.isEmpty()) {
            wrapper.eq(HrCertificate::getCertType, certType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(HrCertificate::getStatus, status);
        }
        wrapper.orderByDesc(HrCertificate::getIssueDate);

        return R.success(hrCertificateService.page(page, wrapper));
    }

    /**
     * 根据ID获取证照
     */
    @GetMapping("/{certificateId}")
    public R<HrCertificate> getById(@PathVariable Long certificateId) {
        return R.success(hrCertificateService.getById(certificateId));
    }

    /**
     * 获取员工证照列表
     */
    @GetMapping("/employee/{employeeId}")
    public R<?> getEmployeeCertificates(@PathVariable Long employeeId) {
        return hrCertificateService.getEmployeeCertificates(employeeId);
    }

    /**
     * 获取即将到期证照
     */
    @GetMapping("/expiring")
    public R<?> getExpiringCertificates(@RequestParam(defaultValue = "30") int days) {
        return hrCertificateService.getExpiringCertificates(days);
    }

    /**
     * 获取特种作业证照
     */
    @GetMapping("/special")
    public R<?> getSpecialOperationCertificates() {
        return hrCertificateService.getSpecialOperationCertificates();
    }

    /**
     * 新增证照
     */
    @PostMapping
    @HrSecurityConfig.RequiresCertificatePermission("add")
    public R<?> createCertificate(@RequestBody HrCertificate certificate) {
        return hrCertificateService.createCertificate(certificate);
    }

    /**
     * 修改证照
     */
    @PutMapping
    @HrSecurityConfig.RequiresCertificatePermission("edit")
    public R<?> updateCertificate(@RequestBody HrCertificate certificate) {
        return hrCertificateService.updateCertificate(certificate);
    }

    /**
     * 证照复审
     */
    @PostMapping("/{certificateId}/review")
    @HrSecurityConfig.RequiresCertificatePermission("review")
    public R<?> handleCertificateReview(@PathVariable Long certificateId,
                                     @RequestParam Date reviewDate,
                                     @RequestParam String result) {
        return hrCertificateService.handleCertificateReview(certificateId, reviewDate, result);
    }

    /**
     * 获取证照状态枚举
     */
    @GetMapping("/statuses")
    public R<List<String>> getCertificateStatuses() {
        List<String> statuses = Arrays.asList("VALID", "INVALID", "EXPIRED", "REVIEWING");
        return R.success(statuses);
    }

    /**
     * 获取证照类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getCertificateTypes() {
        List<String> types = Arrays.asList("ID_CARD", "DEGREE", "QUALIFICATION", "SAFETY", "SPECIAL_OPERATION");
        return R.success(types);
    }

    /**
     * 获取特种作业类型枚举
     */
    @GetMapping("/specialTypes")
    public R<List<String>> getSpecialOperationTypes() {
        List<String> types = Arrays.asList("ELECTRICIAN", "WELDER", "LIFTING", "EXPLOSIVE", "MINING");
        return R.success(types);
    }
}