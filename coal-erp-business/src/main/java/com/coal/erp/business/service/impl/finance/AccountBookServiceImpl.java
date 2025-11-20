package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.finance.AccountBalance;
import com.coal.erp.business.domain.finance.VoucherDetail;
import com.coal.erp.business.mapper.finance.AccountBalanceMapper;
import com.coal.erp.business.mapper.finance.VoucherDetailMapper;
import com.coal.erp.business.service.finance.IAccountBookService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账簿查询服务实现
 */
@Service
public class AccountBookServiceImpl implements IAccountBookService {

    @Autowired
    private AccountBalanceMapper accountBalanceMapper;
    
    @Autowired
    private VoucherDetailMapper voucherDetailMapper;

    @Override
    public R<List<AccountBalance>> getGeneralLedger(String period) {
        LambdaQueryWrapper<AccountBalance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBalance::getPeriod, period)
               .orderByAsc(AccountBalance::getSubjectCode);
        
        List<AccountBalance> balances = accountBalanceMapper.selectList(wrapper);
        return R.success(balances);
    }

    @Override
    public R<List<VoucherDetail>> getDetailLedger(Long subjectId, String period) {
        LambdaQueryWrapper<VoucherDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VoucherDetail::getSubjectId, subjectId)
               .apply("DATE_FORMAT(create_time, '%Y%m') = {0}", period)
               .orderByAsc(VoucherDetail::getCreateTime);
        
        List<VoucherDetail> details = voucherDetailMapper.selectList(wrapper);
        return R.success(details);
    }

    @Override
    public R<Map<String, Object>> getMultiColumnLedger(Long subjectId, String period) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取科目信息
        AccountBalance balance = accountBalanceMapper.selectOne(
            new LambdaQueryWrapper<AccountBalance>()
                .eq(AccountBalance::getSubjectId, subjectId)
                .eq(AccountBalance::getPeriod, period)
        );
        
        if (balance != null) {
            result.put("subjectInfo", balance);
            
            // 获取按部门分类的明细
            List<Map<String, Object>> deptDetails = voucherDetailMapper.selectMaps(
                new LambdaQueryWrapper<VoucherDetail>()
                    .select(VoucherDetail::getDeptId, VoucherDetail::getDirection)
                    .eq(VoucherDetail::getSubjectId, subjectId)
                    .apply("DATE_FORMAT(create_time, '%Y%m') = {0}", period)
                    .groupBy(VoucherDetail::getDeptId, VoucherDetail::getDirection)
            );
            result.put("deptDetails", deptDetails);
        }
        
        return R.success(result);
    }

    @Override
    public R<List<AccountBalance>> getBalanceSheet(String period) {
        LambdaQueryWrapper<AccountBalance> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBalance::getPeriod, period)
               .orderByAsc(AccountBalance::getSubjectCode);
        
        List<AccountBalance> balances = accountBalanceMapper.selectList(wrapper);
        return R.success(balances);
    }

    @Override
    public BigDecimal calculateSubjectBalance(Long subjectId, String period) {
        // 计算借方总额
        BigDecimal debitTotal = voucherDetailMapper.selectSumAmount(
            subjectId, period, "1");
        
        // 计算贷方总额
        BigDecimal creditTotal = voucherDetailMapper.selectSumAmount(
            subjectId, period, "-1");
        
        // 获取期初余额
        AccountBalance prevBalance = accountBalanceMapper.selectOne(
            new LambdaQueryWrapper<AccountBalance>()
                .eq(AccountBalance::getSubjectId, subjectId)
                .eq(AccountBalance::getPeriod, getPrevPeriod(period))
        );
        
        BigDecimal beginBalance = prevBalance != null ? prevBalance.getEndAmount() : BigDecimal.ZERO;
        String beginDirection = prevBalance != null ? prevBalance.getEndDirection() : "1";
        
        // 计算期末余额
        if ("1".equals(beginDirection)) {
            return beginBalance.add(debitTotal).subtract(creditTotal);
        } else {
            return beginBalance.add(creditTotal).subtract(debitTotal);
        }
    }

    @Override
    public void updateAccountBalance(String period) {
        // 获取所有科目
        List<Long> subjectIds = voucherDetailMapper.selectDistinctSubjectIds(period);
        
        for (Long subjectId : subjectIds) {
            BigDecimal balance = calculateSubjectBalance(subjectId, period);
            String direction = balance.compareTo(BigDecimal.ZERO) >= 0 ? "1" : "-1";
            
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.setSubjectId(subjectId);
            accountBalance.setPeriod(period);
            accountBalance.setEndAmount(balance.abs());
            accountBalance.setEndDirection(direction);
            
            // 更新或插入余额记录
            AccountBalance existing = accountBalanceMapper.selectOne(
                new LambdaQueryWrapper<AccountBalance>()
                    .eq(AccountBalance::getSubjectId, subjectId)
                    .eq(AccountBalance::getPeriod, period)
            );
            
            if (existing != null) {
                accountBalance.setBalanceId(existing.getBalanceId());
                accountBalanceMapper.updateById(accountBalance);
            } else {
                accountBalanceMapper.insert(accountBalance);
            }
        }
    }

    /**
     * 获取上一个会计期间
     */
    private String getPrevPeriod(String period) {
        int year = Integer.parseInt(period.substring(0, 4));
        int month = Integer.parseInt(period.substring(4));
        
        if (month == 1) {
            year--;
            month = 12;
        } else {
            month--;
        }
        
        return String.format("%04d%02d", year, month);
    }
}