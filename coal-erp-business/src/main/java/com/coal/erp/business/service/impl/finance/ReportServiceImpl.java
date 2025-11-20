package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.finance.AccountBalance;
import com.coal.erp.business.domain.finance.AccountSubject;
import com.coal.erp.business.mapper.finance.AccountBalanceMapper;
import com.coal.erp.business.mapper.finance.AccountSubjectMapper;
import com.coal.erp.business.service.finance.IReportService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 财务报表服务实现
 */
@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private AccountBalanceMapper accountBalanceMapper;
    
    @Autowired
    private AccountSubjectMapper accountSubjectMapper;

    @Override
    public R<Map<String, Object>> generateBalanceSheet(String period) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取资产类科目余额
        List<AccountBalance> assets = accountBalanceMapper.selectList(
            new LambdaQueryWrapper<AccountBalance>()
                .eq(AccountBalance::getPeriod, period)
                .inSql(AccountBalance::getSubjectId, 
                    "SELECT subject_id FROM account_subject WHERE subject_type = 'ASSET'")
                .orderByAsc(AccountBalance::getSubjectCode)
        );
        
        // 获取负债和权益类科目余额
        List<AccountBalance> liabilities = accountBalanceMapper.selectList(
            new LambdaQueryWrapper<AccountBalance>()
                .eq(AccountBalance::getPeriod, period)
                .inSql(AccountBalance::getSubjectId, 
                    "SELECT subject_id FROM account_subject WHERE subject_type IN ('LIABILITY', 'EQUITY')")
                .orderByAsc(AccountBalance::getSubjectCode)
        );
        
        // 计算资产总计
        BigDecimal totalAssets = assets.stream()
            .map(b -> "1".equals(b.getEndDirection()) ? b.getEndAmount() : b.getEndAmount().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算负债和权益总计
        BigDecimal totalLiabilities = liabilities.stream()
            .map(b -> "-1".equals(b.getEndDirection()) ? b.getEndAmount() : b.getEndAmount().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        result.put("assets", assets);
        result.put("liabilities", liabilities);
        result.put("totalAssets", totalAssets);
        result.put("totalLiabilities", totalLiabilities);
        result.put("period", period);
        
        return R.success(result);
    }

    @Override
    public R<Map<String, Object>> generateProfitStatement(String period) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取损益类科目余额
        List<AccountBalance> profitLossItems = accountBalanceMapper.selectList(
            new LambdaQueryWrapper<AccountBalance>()
                .eq(AccountBalance::getPeriod, period)
                .inSql(AccountBalance::getSubjectId, 
                    "SELECT subject_id FROM account_subject WHERE subject_type = 'PROFIT'")
                .orderByAsc(AccountBalance::getSubjectCode)
        );
        
        // 分类收入和费用
        Map<String, List<AccountBalance>> categorized = profitLossItems.stream()
            .collect(Collectors.groupingBy(b -> {
                AccountSubject subject = accountSubjectMapper.selectById(b.getSubjectId());
                return subject.getSubjectCode().startsWith("6") ? "income" : "expense";
            }));
        
        // 计算总收入
        BigDecimal totalIncome = categorized.getOrDefault("income", Collections.emptyList()).stream()
            .map(b -> "-1".equals(b.getEndDirection()) ? b.getEndAmount() : b.getEndAmount().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算总费用
        BigDecimal totalExpense = categorized.getOrDefault("expense", Collections.emptyList()).stream()
            .map(b -> "1".equals(b.getEndDirection()) ? b.getEndAmount() : b.getEndAmount().negate())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 计算净利润
        BigDecimal netProfit = totalIncome.subtract(totalExpense);
        
        result.put("incomeItems", categorized.getOrDefault("income", Collections.emptyList()));
        result.put("expenseItems", categorized.getOrDefault("expense", Collections.emptyList()));
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("netProfit", netProfit);
        result.put("period", period);
        
        return R.success(result);
    }

    @Override
    public R<Map<String, Object>> generateCashFlowStatement(String period) {
        // TODO: 实现现金流量表逻辑
        return R.success(new HashMap<>());
    }

    @Override
    public R<String> exportReportToExcel(String reportType, String period) {
        // TODO: 实现Excel导出功能
        return R.success("报表导出功能待实现");
    }
}