package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.finance.FinancePayment;
import com.coal.erp.business.service.finance.IFinancePaymentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 收付款控制器
 */
@RestController
@RequestMapping("/finance/payment")
public class FinancePaymentController {

    @Autowired
    private IFinancePaymentService financePaymentService;

    /**
     * 分页查询收付款单
     */
    @GetMapping("/page")
    public R<Page<FinancePayment>> page(@RequestParam(defaultValue = "1") Long current,
                                       @RequestParam(defaultValue = "10") Long size,
                                       @RequestParam(required = false) Long customerId,
                                       @RequestParam(required = false) Long supplierId,
                                       @RequestParam(required = false) String paymentType,
                                       @RequestParam(required = false) String status) {
        Page<FinancePayment> page = new Page<>(current, size);
        LambdaQueryWrapper<FinancePayment> wrapper = new LambdaQueryWrapper<>();
        
        if (customerId != null) {
            wrapper.eq(FinancePayment::getCustomerId, customerId);
        }
        if (supplierId != null) {
            wrapper.eq(FinancePayment::getSupplierId, supplierId);
        }
        if (paymentType != null && !paymentType.isEmpty()) {
            wrapper.eq(FinancePayment::getPaymentType, paymentType);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(FinancePayment::getStatus, status);
        }
        wrapper.orderByDesc(FinancePayment::getPaymentDate);

        return R.success(financePaymentService.page(page, wrapper));
    }

    /**
     * 获取收付款单详情
     */
    @GetMapping("/{paymentId}")
    public R<FinancePayment> getById(@PathVariable Long paymentId) {
        FinancePayment payment = financePaymentService.getById(paymentId);
        return payment != null ? R.success(payment) : R.fail("收付款单不存在");
    }

    /**
     * 创建收款单
     */
    @PostMapping("/receive")
    public R<?> createReceivePayment(@RequestBody FinancePayment payment,
                                    @RequestParam(required = false) List<Long> receivableIds) {
        return financePaymentService.createReceivePayment(payment, receivableIds);
    }

    /**
     * 创建付款单
     */
    @PostMapping("/pay")
    public R<?> createPayPayment(@RequestBody FinancePayment payment,
                                @RequestParam(required = false) List<Long> payableIds) {
        return financePaymentService.createPayPayment(payment, payableIds);
    }

    /**
     * 创建预收预付单
     */
    @PostMapping("/advance")
    public R<?> createAdvancePayment(@RequestBody FinancePayment payment) {
        return financePaymentService.createAdvancePayment(payment);
    }

    /**
     * 确认收付款单
     */
    @PostMapping("/confirm/{paymentId}")
    public R<?> confirmPayment(@PathVariable Long paymentId) {
        return financePaymentService.confirmPayment(paymentId);
    }

    /**
     * 取消收付款单
     */
    @PostMapping("/cancel/{paymentId}")
    public R<?> cancelPayment(@PathVariable Long paymentId, @RequestParam String reason) {
        return financePaymentService.cancelPayment(paymentId, reason);
    }

    /**
     * 核销预收预付
     */
    @PostMapping("/settle-advance/{advancePaymentId}")
    public R<?> settleAdvancePayment(@PathVariable Long advancePaymentId,
                                    @RequestBody List<Long> sourceIds) {
        return financePaymentService.settleAdvancePayment(advancePaymentId, sourceIds);
    }

    /**
     * 获取客户收款记录
     */
    @GetMapping("/customer-history/{customerId}")
    public R<?> getCustomerPaymentHistory(@PathVariable Long customerId) {
        return financePaymentService.getCustomerPaymentHistory(customerId);
    }

    /**
     * 获取供应商付款记录
     */
    @GetMapping("/supplier-history/{supplierId}")
    public R<?> getSupplierPaymentHistory(@PathVariable Long supplierId) {
        return financePaymentService.getSupplierPaymentHistory(supplierId);
    }

    /**
     * 处理多币种结算
     */
    @PostMapping("/currency-convert/{paymentId}")
    public R<?> handleMultiCurrencySettlement(
        @PathVariable Long paymentId,
        @RequestParam String targetCurrency,
        @RequestParam BigDecimal exchangeRate) {
        return financePaymentService.handleMultiCurrencySettlement(paymentId, targetCurrency, exchangeRate);
    }

    /**
     * 获取收付款类型枚举
     */
    @GetMapping("/payment-types")
    public R<List<String>> getPaymentTypes() {
        List<String> paymentTypes = Arrays.asList(
            "RECEIVE",  // 收款
            "PAY",      // 付款
            "ADVANCE"   // 预收预付
        );
        return R.success(paymentTypes);
    }

    /**
     * 获取收付款状态枚举
     */
    @GetMapping("/status-types")
    public R<List<String>> getStatusTypes() {
        List<String> statusTypes = Arrays.asList(
            "UNCONFIRMED",  // 未确认
            "CONFIRMED",    // 已确认
            "CANCELLED"     // 已取消
        );
        return R.success(statusTypes);
    }
}