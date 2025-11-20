package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.finance.FinanceReceivable;
import com.coal.erp.business.service.finance.IFinanceReceivableService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

/**
 * 应收单据控制器
 */
@RestController
@RequestMapping("/finance/receivable")
public class FinanceReceivableController {

    @Autowired
    private IFinanceReceivableService financeReceivableService;

    /**
     * 分页查询应收单据
     */
    @GetMapping("/page")
    public R<Page<FinanceReceivable>> page(@RequestParam(defaultValue = "1") Long current,
                                          @RequestParam(defaultValue = "10") Long size,
                                          @RequestParam(required = false) Long customerId,
                                          @RequestParam(required = false) String status) {
        Page<FinanceReceivable> page = new Page<>(current, size);
        LambdaQueryWrapper<FinanceReceivable> wrapper = new LambdaQueryWrapper<>();
        
        if (customerId != null) {
            wrapper.eq(FinanceReceivable::getCustomerId, customerId);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(FinanceReceivable::getStatus, status);
        }
        wrapper.orderByDesc(FinanceReceivable::getIssueDate);

        return R.success(financeReceivableService.page(page, wrapper));
    }

    /**
     * 获取应收单据详情
     */
    @GetMapping("/{receivableId}")
    public R<FinanceReceivable> getById(@PathVariable Long receivableId) {
        FinanceReceivable receivable = financeReceivableService.getById(receivableId);
        return receivable != null ? R.success(receivable) : R.fail("应收单据不存在");
    }

    /**
     * 创建应收单据
     */
    @PostMapping
    public R<?> create(@RequestBody FinanceReceivable receivable) {
        return financeReceivableService.createReceivable(receivable);
    }

    /**
     * 更新应收单据
     */
    @PutMapping
    public R<?> update(@RequestBody FinanceReceivable receivable) {
        return financeReceivableService.updateReceivable(receivable);
    }

    /**
     * 核销应收单据
     */
    @PostMapping("/settle/{receivableId}")
    public R<?> settle(@PathVariable Long receivableId, @RequestParam BigDecimal amount) {
        return financeReceivableService.settleReceivable(receivableId, amount);
    }

    /**
     * 作废应收单据
     */
    @PostMapping("/cancel/{receivableId}")
    public R<?> cancel(@PathVariable Long receivableId, @RequestParam String reason) {
        return financeReceivableService.cancelReceivable(receivableId, reason);
    }

    /**
     * 获取客户应收余额
     */
    @GetMapping("/balance/{customerId}")
    public R<?> getBalance(@PathVariable Long customerId) {
        return financeReceivableService.getCustomerReceivableBalance(customerId);
    }

    /**
     * 获取账龄分析报告
     */
    @GetMapping("/aging-analysis")
    public R<?> getAgingAnalysis(@RequestParam(required = false) Date asOfDate) {
        if (asOfDate == null) {
            asOfDate = new Date();
        }
        return financeReceivableService.getAgingAnalysisReport(asOfDate);
    }

    /**
     * 计提坏账准备
     */
    @PostMapping("/provision-bad-debt/{receivableId}")
    public R<?> provisionBadDebt(@PathVariable Long receivableId, @RequestParam BigDecimal amount) {
        return financeReceivableService.provisionBadDebt(receivableId, amount);
    }

    /**
     * 核销坏账
     */
    @PostMapping("/write-off/{receivableId}")
    public R<?> writeOffBadDebt(@PathVariable Long receivableId) {
        return financeReceivableService.writeOffBadDebt(receivableId);
    }

    /**
     * 收回已核销坏账
     */
    @PostMapping("/recover/{receivableId}")
    public R<?> recoverBadDebt(@PathVariable Long receivableId) {
        return financeReceivableService.recoverBadDebt(receivableId);
    }

    /**
     * 获取应收状态枚举
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