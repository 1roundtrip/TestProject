package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.FinancePayable;
import com.coal.erp.business.mapper.finance.FinancePayableMapper;
import com.coal.erp.business.service.finance.IFinancePayableService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 应付单据服务实现
 */
@Service
public class FinancePayableServiceImpl 
    extends ServiceImpl<FinancePayableMapper, FinancePayable> 
    implements IFinancePayableService {

    @Override
    public R<?> createPayable(FinancePayable payable) {
        // 设置默认值
        if (payable.getPaidAmount() == null) {
            payable.setPaidAmount(BigDecimal.ZERO);
        }
        if (payable.getBalanceAmount() == null) {
            payable.setBalanceAmount(payable.getAmount());
        }
        if (payable.getStatus() == null) {
            payable.setStatus("UNPAID");
        }
        if (payable.getCurrency() == null) {
            payable.setCurrency("CNY");
        }
        if (payable.getExchangeRate() == null) {
            payable.setExchangeRate(BigDecimal.ONE);
        }
        
        boolean success = save(payable);
        return success ? R.success(payable) : R.fail("创建应付单据失败");
    }

    @Override
    public R<?> updatePayable(FinancePayable payable) {
        boolean success = updateById(payable);
        return success ? R.success(payable) : R.fail("更新应付单据失败");
    }

    @Override
    public R<?> settlePayable(Long payableId, BigDecimal settleAmount) {
        FinancePayable payable = getById(payableId);
        if (payable == null) {
            return R.fail("应付单据不存在");
        }
        
        BigDecimal newPaid = payable.getPaidAmount().add(settleAmount);
        BigDecimal newBalance = payable.getAmount().subtract(newPaid);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return R.fail("核销金额超过应付余额");
        }
        
        payable.setPaidAmount(newPaid);
        payable.setBalanceAmount(newBalance);
        
        // 更新状态
        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            payable.setStatus("PAID");
        } else {
            payable.setStatus("PARTIAL");
        }
        
        boolean success = updateById(payable);
        return success ? R.success(payable) : R.fail("核销失败");
    }

    @Override
    public R<?> cancelPayable(Long payableId, String reason) {
        FinancePayable payable = getById(payableId);
        if (payable == null) {
            return R.fail("应付单据不存在");
        }
        
        if (!"UNPAID".equals(payable.getStatus())) {
            return R.fail("只能取消未付款的单据");
        }
        
        payable.setStatus("CANCELLED");
        boolean success = updateById(payable);
        return success ? R.success(payable) : R.fail("取消单据失败");
    }

    @Override
    public R<?> getSupplierPayableBalance(Long supplierId) {
        List<FinancePayable> payables = lambdaQuery()
            .eq(FinancePayable::getSupplierId, supplierId)
            .ne(FinancePayable::getStatus, "CANCELLED")
            .list();
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;
        
        for (FinancePayable p : payables) {
            totalAmount = totalAmount.add(p.getAmount());
            totalBalance = totalBalance.add(p.getBalanceAmount());
        }
        
        final BigDecimal finalTotalAmount = totalAmount;
        final BigDecimal finalTotalBalance = totalBalance;
        final int finalCount = payables.size();
        
        return R.success(new Object() {
            public BigDecimal totalAmount = finalTotalAmount;
            public BigDecimal totalBalance = finalTotalBalance;
            public int count = finalCount;
        });
    }

    @Override
    public R<?> getPayableAgingAnalysis(Date asOfDate) {
        // TODO: 实现应付账龄分析逻辑
        return R.success("应付账龄分析完成");
    }

    @Override
    public R<?> createPaymentPlan(Long payableId, Date planDate, BigDecimal amount) {
        // TODO: 实现付款计划创建
        return R.success("付款计划创建成功");
    }

    @Override
    public R<?> handleAdvancePayment(Long supplierId, BigDecimal amount) {
        // TODO: 实现预付款处理逻辑
        FinancePayable advancePayable = new FinancePayable();
        advancePayable.setSupplierId(supplierId);
        advancePayable.setAmount(amount);
        advancePayable.setSourceType("ADVANCE");
        advancePayable.setDescription("设备采购预付款");
        advancePayable.setStatus("PAID"); // 预付款立即支付
        
        boolean success = save(advancePayable);
        return success ? R.success(advancePayable) : R.fail("预付款处理失败");
    }
}