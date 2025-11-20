package com.coal.erp.business.controller.hr;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.HrSecurityConfig;
import com.coal.erp.business.domain.hr.HrContract;
import com.coal.erp.business.service.hr.HrContractService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 合同管理控制器
 */
@RestController
@RequestMapping("/hr/contract")
@HrSecurityConfig.RequiresContractPermission
public class HrContractController {

    @Autowired
    private HrContractService hrContractService;

    /**
     * 分页查询合同
     */
    @GetMapping("/page")
    public R<Page<HrContract>> page(@RequestParam(defaultValue = "1") Long current,
                                   @RequestParam(defaultValue = "10") Long size,
                                   @RequestParam(required = false) String contractNo,
                                   @RequestParam(required = false) String status) {
        Page<HrContract> page = new Page<>(current, size);
        LambdaQueryWrapper<HrContract> wrapper = new LambdaQueryWrapper<>();
        
        if (contractNo != null && !contractNo.isEmpty()) {
            wrapper.like(HrContract::getContractNo, contractNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(HrContract::getStatus, status);
        }
        wrapper.orderByDesc(HrContract::getSignDate);

        return R.success(hrContractService.page(page, wrapper));
    }

    /**
     * 根据ID获取合同
     */
    @GetMapping("/{contractId}")
    public R<HrContract> getById(@PathVariable Long contractId) {
        return R.success(hrContractService.getById(contractId));
    }

    /**
     * 获取员工合同列表
     */
    @GetMapping("/employee/{employeeId}")
    public R<?> getEmployeeContracts(@PathVariable Long employeeId) {
        return hrContractService.getEmployeeContracts(employeeId);
    }

    /**
     * 获取即将到期合同
     */
    @GetMapping("/expiring")
    public R<?> getExpiringContracts(@RequestParam(defaultValue = "30") int days) {
        return hrContractService.getExpiringContracts(days);
    }

    /**
     * 新增合同
     */
    @PostMapping
    @HrSecurityConfig.RequiresContractPermission("add")
    public R<?> createContract(@RequestBody HrContract contract) {
        return hrContractService.createContract(contract);
    }

    /**
     * 修改合同
     */
    @PutMapping
    @HrSecurityConfig.RequiresContractPermission("edit")
    public R<?> updateContract(@RequestBody HrContract contract) {
        return hrContractService.updateContract(contract);
    }

    /**
     * 终止合同
     */
    @PostMapping("/{contractId}/terminate")
    @HrSecurityConfig.RequiresContractPermission("terminate")
    public R<?> terminateContract(@PathVariable Long contractId,
                                 @RequestParam String reason) {
        return hrContractService.terminateContract(contractId, reason);
    }

    /**
     * 续签合同
     */
    @PostMapping("/{contractId}/renew")
    @HrSecurityConfig.RequiresContractPermission("renew")
    public R<?> renewContract(@PathVariable Long contractId,
                             @RequestParam Integer renewPeriod) {
        return hrContractService.renewContract(contractId, renewPeriod);
    }

    /**
     * 获取合同状态枚举
     */
    @GetMapping("/statuses")
    public R<List<String>> getContractStatuses() {
        List<String> statuses = Arrays.asList("ACTIVE", "TERMINATED", "RENEWED", "EXPIRED");
        return R.success(statuses);
    }

    /**
     * 获取合同类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getContractTypes() {
        List<String> types = Arrays.asList("FIXED_TERM", "OPEN_ENDED", "PROBATION", "PART_TIME");
        return R.success(types);
    }
}
