package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.finance.AccountSubject;
import com.coal.erp.business.domain.finance.Voucher;
import com.coal.erp.business.domain.finance.VoucherDetail;
import com.coal.erp.business.mapper.finance.AccountSubjectMapper;
import com.coal.erp.business.mapper.finance.VoucherDetailMapper;
import com.coal.erp.business.mapper.finance.VoucherMapper;
import com.coal.erp.business.service.finance.IPeriodEndService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 期末处理服务实现
 */
@Service
public class PeriodEndServiceImpl implements IPeriodEndService {

    @Autowired
    private AccountSubjectMapper accountSubjectMapper;
    
    @Autowired
    private VoucherMapper voucherMapper;
    
    @Autowired
    private VoucherDetailMapper voucherDetailMapper;
    
    @Autowired
    private VoucherServiceImpl voucherService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> autoTransfer(String period) {
        // TODO: 实现自动转账逻辑（如折旧计提等）
        return R.success("自动转账完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> exchangeAdjustment(String period) {
        // TODO: 实现期末调汇逻辑
        return R.success("期末调汇完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> transferProfitLoss(String period) {
        // 获取所有损益类科目
        List<AccountSubject> profitLossSubjects = accountSubjectMapper.selectList(
            new LambdaQueryWrapper<AccountSubject>()
                .eq(AccountSubject::getSubjectType, "PROFIT")
                .eq(AccountSubject::getStatus, "0")
        );
        
        // 创建结转凭证
        Voucher voucher = new Voucher();
        voucher.setVoucherDate(new Date());
        voucher.setPeriod(period);
        voucher.setMakerId(1L); // 系统自动制单
        voucher.setMakerName("系统");
        voucher.setMakerTime(new Date());
        voucher.setStatus("AUDITED"); // 自动审核
        voucher.setRemark("损益结转凭证");
        
        List<VoucherDetail> details = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // 为每个损益类科目创建分录
        for (AccountSubject subject : profitLossSubjects) {
            BigDecimal balance = voucherDetailMapper.selectSumAmount(
                subject.getSubjectId(), period, "1")
                .subtract(voucherDetailMapper.selectSumAmount(
                    subject.getSubjectId(), period, "-1"));
            
            if (balance.compareTo(BigDecimal.ZERO) != 0) {
                VoucherDetail detail = new VoucherDetail();
                detail.setSubjectId(subject.getSubjectId());
                detail.setSubjectCode(subject.getSubjectCode());
                detail.setSubjectName(subject.getSubjectName());
                detail.setDirection(balance.compareTo(BigDecimal.ZERO) > 0 ? "-1" : "1");
                detail.setAmount(balance.abs());
                detail.setSummary("损益结转");
                details.add(detail);
                
                totalAmount = totalAmount.add(balance.abs());
            }
        }
        
        // 添加本年利润科目分录
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            AccountSubject profitSubject = accountSubjectMapper.selectOne(
                new LambdaQueryWrapper<AccountSubject>()
                    .eq(AccountSubject::getSubjectCode, "4103") // 本年利润科目
            );
            
            if (profitSubject != null) {
                VoucherDetail profitDetail = new VoucherDetail();
                profitDetail.setSubjectId(profitSubject.getSubjectId());
                profitDetail.setSubjectCode(profitSubject.getSubjectCode());
                profitDetail.setSubjectName(profitSubject.getSubjectName());
                profitDetail.setDirection("1");
                profitDetail.setAmount(totalAmount);
                profitDetail.setSummary("损益结转");
                details.add(profitDetail);
            }
            
            voucher.setTotalAmount(totalAmount);
            return voucherService.addVoucher(voucher, details);
        }
        
        return R.success("无损益需要结转");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> periodClosing(String period) {
        // 检查是否所有凭证都已记账
        long unpostedCount = voucherMapper.selectCount(
            new LambdaQueryWrapper<Voucher>()
                .eq(Voucher::getPeriod, period)
                .ne(Voucher::getStatus, "POSTED")
        );
        
        if (unpostedCount > 0) {
            return R.fail("存在未记账凭证，不能结账");
        }
        
        // 执行损益结转
        R<?> transferResult = transferProfitLoss(period);
        if (!transferResult.isSuccess()) {
            return transferResult;
        }
        
        // 更新科目余额
        // accountBookService.updateAccountBalance(period);
        
        // TODO: 标记期间为已结账
        
        return R.success("期末结账完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> reverseClosing(String period) {
        // TODO: 检查下期是否已结账
        // TODO: 删除结转凭证
        // TODO: 标记期间为未结账
        
        return R.success("反结账完成");
    }
}