package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.FinancePayment;
import com.coal.erp.business.mapper.finance.FinancePaymentMapper;
import com.coal.erp.business.service.finance.IFinancePaymentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 收付款服务实现
 */
@Service
public class FinancePaymentServiceImpl 
    extends ServiceImpl<FinancePaymentMapper, FinancePayment> 
    implements IFinancePaymentService {

    @Override
    public R<?> createReceivePayment(FinancePayment payment, List<Long> receivableIds) {
        // 设置默认值
        if (payment.getCurrency() == null) {
            payment.setCurrency("CNY");
        }
        if (payment.getExchangeRate() == null) {
            payment.setExchangeRate(BigDecimal.ONE);
        }
        if (payment.getStatus() == null) {
            payment.setStatus("UNCONFIRMED");
        }
        
        boolean success = save(payment);
        return success ? R.success(payment) : R.fail("创建收款单失败");
    }

    @Override
    public R<?> createPayPayment(FinancePayment payment, List<Long> payableIds) {
        // 设置默认值
        if (payment.getCurrency() == null) {
            payment.setCurrency("CNY");
        }
        if (payment.getExchangeRate() == null) {
            payment.setExchangeRate(BigDecimal.ONE);
        }
        if (payment.getStatus() == null) {
            payment.setStatus("UNCONFIRMED");
        }
        
        boolean success = save(payment);
        return success ? R.success(payment) : R.fail("创建付款单失败");
    }

    @Override
    public R<?> createAdvancePayment(FinancePayment payment) {
        // 设置默认值
        if (payment.getCurrency() == null) {
            payment.setCurrency("CNY");
        }
        if (payment.getExchangeRate() == null) {
            payment.setExchangeRate(BigDecimal.ONE);
        }
        if (payment.getStatus() == null) {
            payment.setStatus("UNCONFIRMED");
        }
        
        boolean success = save(payment);
        return success ? R.success(payment) : R.fail("创建预收预付单失败");
    }

    @Override
    public R<?> confirmPayment(Long paymentId) {
        FinancePayment payment = getById(paymentId);
        if (payment == null) {
            return R.fail("收付款单不存在");
        }
        
        payment.setStatus("CONFIRMED");
        boolean success = updateById(payment);
        return success ? R.success(payment) : R.fail("确认收付款单失败");
    }

    @Override
    public R<?> cancelPayment(Long paymentId, String reason) {
        FinancePayment payment = getById(paymentId);
        if (payment == null) {
            return R.fail("收付款单不存在");
        }
        
        payment.setStatus("CANCELLED");
        boolean success = updateById(payment);
        return success ? R.success(payment) : R.fail("取消收付款单失败");
    }

    @Override
    public R<?> settleAdvancePayment(Long advancePaymentId, List<Long> sourceIds) {
        // TODO: 实现预收预付核销逻辑
        return R.success("预收预付核销成功");
    }

    @Override
    public R<?> getCustomerPaymentHistory(Long customerId) {
        List<FinancePayment> payments = lambdaQuery()
            .eq(FinancePayment::getCustomerId, customerId)
            .ne(FinancePayment::getStatus, "CANCELLED")
            .orderByDesc(FinancePayment::getPaymentDate)
            .list();
        return R.success(payments);
    }

    @Override
    public R<?> getSupplierPaymentHistory(Long supplierId) {
        List<FinancePayment> payments = lambdaQuery()
            .eq(FinancePayment::getSupplierId, supplierId)
            .ne(FinancePayment::getStatus, "CANCELLED")
            .orderByDesc(FinancePayment::getPaymentDate)
            .list();
        return R.success(payments);
    }

    @Override
    public R<?> handleMultiCurrencySettlement(Long paymentId, String targetCurrency, BigDecimal exchangeRate) {
        FinancePayment payment = getById(paymentId);
        if (payment == null) {
            return R.fail("收付款单不存在");
        }
        
        // 计算汇率换算后的金额
        BigDecimal originalAmount = payment.getAmount();
        BigDecimal convertedAmount = originalAmount.multiply(exchangeRate);
        
        payment.setCurrency(targetCurrency);
        payment.setExchangeRate(exchangeRate);
        payment.setAmount(convertedAmount);
        
        boolean success = updateById(payment);
        return success ? R.success(payment) : R.fail("多币种结算处理失败");
    }
}