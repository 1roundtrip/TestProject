package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.finance.FinancePayable;
import com.coal.erp.business.service.finance.IFinancePayableService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 * 应付单据控制器
 */
@RestController
@RequestMapping("/finance/payable")
public class FinancePayableController {

    @Autowired
    private IFinancePayableService financePayableService;

    /**
     * 分页查询应付单据
     */
    @GetMapping("/page")
    public R<Page<FinancePayable>> page(@RequestParam(defaultValue = "1") Long current,
                                       @RequestParam(defaultValue = "10") Long size,
                                       @RequestParam(required = false) Long supplierId,
                                       @RequestParam(required = false) String status) {
        Page<FinancePayable> page = new Page<>(current, size);
        LambdaQueryWrapper<FinancePayable> wrapper = new LambdaQueryWrapper<>();
        
        if (supplierId != null) {
            wrapper.eq(FinancePayable::getSupplierId, supplierId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(FinancePayable::getStatus, status);
        }
        wrapper.orderByDesc(FinancePayable::getIssueDate);

        return R.success(financePayableService.page(page, wrapper));
    }

    /**
     * 获取应付单据详情
     */
    @GetMapping("/{payableId}")
    public R<FinancePayable> getById(@PathVariable Long payableId) {
        FinancePayable payable = financePayableService.getById(payableId);
        return payable != null ? R.success(payable) : R.fail("应付单据不存在");
    }

    /**
     * 创建应付单据
     */
    @PostMapping
    public R<?> create(@RequestBody FinancePayable payable) {
        return financePayableService.createPayable(payable);
    }

    /**
     * 更新应付单据
     */
    @PutMapping
    public R<?> update(@RequestBody FinancePayable payable) {
        return financePayableService.updatePayable(payable);
    }

    /**
     * 核销应付单据
     */
    @PostMapping("/settle/{payableId}")
    public R<?> settle(@PathVariable Long payableId, @RequestParam BigDecimal amount) {
        return financePayableService.settlePayable(payableId, amount);
    }

    /**
     * 作废应付单据
     */
    @PostMapping("/cancel/{payableId}")
    public R<?> cancel(@PathVariable Long payableId, @RequestParam String reason) {
        return financePayableService.cancelPayable(payableId, reason);
    }

    /**
     * 获取供应商应付余额
     */
    @GetMapping("/balance/{supplierId}")
    public R<?> getBalance(@PathVariable Long supplierId) {
        return financePayableService.getSupplierPayableBalance(supplierId);
    }

    /**
     * 获取应付账龄分析
     */
    @GetMapping("/aging-analysis")
    public R<?> getAgingAnalysis(@RequestParam(required = false) Date asOfDate) {
        if (asOfDate == null) {
            asOfDate = new Date();
        }
        return financePayableService.getPayableAgingAnalysis(asOfDate);
    }

    /**
     * 创建付款计划
     */
    @PostMapping("/payment-plan/{payableId}")
    public R<?> createPaymentPlan(
        @PathVariable Long payableId,
        @RequestParam Date planDate,
        @RequestParam BigDecimal amount) {
        return financePayableService.createPaymentPlan(payableId, planDate, amount);
    }

    /**
     * 处理预付款
     */
    @PostMapping("/advance-payment/{supplierId}")
    public R<?> handleAdvancePayment(
        @PathVariable Long supplierId,
        @RequestParam BigDecimal amount) {
        return financePayableService.handleAdvancePayment(supplierId, amount);
    }

    /**
     * 获取应付状态枚举
     */
    @GetMapping("/status-types")
    public R<List<String>> getStatusTypes() {
        List<String> statusTypes = Arrays.asList(
            "UNPAID",    // 未付
            "PARTIAL",   // 部分付
            "PAID",      // 已付
            "CANCELLED"  // 已取消
        );
        return R.success(statusTypes);
    }
}