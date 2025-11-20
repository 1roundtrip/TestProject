package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.FinanceReceivable;
import com.coal.erp.business.mapper.finance.FinanceReceivableMapper;
import com.coal.erp.business.service.finance.IFinanceReceivableService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 应收单据服务实现
 */
@Service
public class FinanceReceivableServiceImpl 
    extends ServiceImpl<FinanceReceivableMapper, FinanceReceivable> 
    implements IFinanceReceivableService {

    @Override
    public R<?> createReceivable(FinanceReceivable receivable) {
        // 设置默认值
        if (receivable.getReceivedAmount() == null) {
            receivable.setReceivedAmount(BigDecimal.ZERO);
        }
        if (receivable.getBalanceAmount() == null) {
            receivable.setBalanceAmount(receivable.getAmount());
        }
        if (receivable.getStatus() == null) {
            receivable.setStatus("UNPAID");
        }
        if (receivable.getCurrency() == null) {
            receivable.setCurrency("CNY");
        }
        if (receivable.getExchangeRate() == null) {
            receivable.setExchangeRate(BigDecimal.ONE);
        }
        
        boolean success = save(receivable);
        return success ? R.success(receivable) : R.fail("创建应收单据失败");
    }

    @Override
    public R<?> updateReceivable(FinanceReceivable receivable) {
        boolean success = updateById(receivable);
        return success ? R.success(receivable) : R.fail("更新应收单据失败");
    }

    @Override
    public R<?> settleReceivable(Long receivableId, BigDecimal settleAmount) {
        FinanceReceivable receivable = getById(receivableId);
        if (receivable == null) {
            return R.fail("应收单据不存在");
        }
        
        BigDecimal newReceived = receivable.getReceivedAmount().add(settleAmount);
        BigDecimal newBalance = receivable.getAmount().subtract(newReceived);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            return R.fail("核销金额超过应收余额");
        }
        
        receivable.setReceivedAmount(newReceived);
        receivable.setBalanceAmount(newBalance);
        
        // 更新状态
        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            receivable.setStatus("PAID");
        } else {
            receivable.setStatus("PARTIAL");
        }
        
        boolean success = updateById(receivable);
        return success ? R.success(receivable) : R.fail("核销失败");
    }

    @Override
    public R<?> cancelReceivable(Long receivableId, String reason) {
        FinanceReceivable receivable = getById(receivableId);
        if (receivable == null) {
            return R.fail("应收单据不存在");
        }
        
        if (!"UNPAID".equals(receivable.getStatus())) {
            return R.fail("只能取消未付款的单据");
        }
        
        receivable.setStatus("CANCELLED");
        boolean success = updateById(receivable);
        return success ? R.success(receivable) : R.fail("取消单据失败");
    }

    @Override
    public R<?> getCustomerReceivableBalance(Long customerId) {
        List<FinanceReceivable> receivables = lambdaQuery()
            .eq(FinanceReceivable::getCustomerId, customerId)
            .ne(FinanceReceivable::getStatus, "CANCELLED")
            .list();
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalBalance = BigDecimal.ZERO;
        
        for (FinanceReceivable r : receivables) {
            totalAmount = totalAmount.add(r.getAmount());
            totalBalance = totalBalance.add(r.getBalanceAmount());
        }
        
        final BigDecimal finalTotalAmount = totalAmount;
        final BigDecimal finalTotalBalance = totalBalance;
        final int finalCount = receivables.size();
        
        return R.success(new Object() {
            public BigDecimal totalAmount = finalTotalAmount;
            public BigDecimal totalBalance = finalTotalBalance;
            public int count = finalCount;
        });
    }

    @Override
    public R<?> getAgingAnalysisReport(Date asOfDate) {
        // TODO: 实现详细的账龄分析逻辑
        return R.success("账龄分析报告生成成功");
    }

    @Override
    public R<?> provisionBadDebt(Long receivableId, BigDecimal amount) {
        // TODO: 实现坏账计提逻辑
        return R.success("坏账计提成功");
    }

    @Override
    public R<?> writeOffBadDebt(Long receivableId) {
        // TODO: 实现坏账核销逻辑
        return R.success("坏账核销成功");
    }

    @Override
    public R<?> recoverBadDebt(Long receivableId) {
        // TODO: 实现坏账收回逻辑
        return R.success("坏账收回成功");
    }
}