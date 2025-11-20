package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.Voucher;
import com.coal.erp.business.domain.finance.VoucherDetail;
import com.coal.erp.business.mapper.finance.VoucherDetailMapper;
import com.coal.erp.business.mapper.finance.VoucherMapper;
import com.coal.erp.business.service.finance.IVoucherService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证服务实现
 */
@Service
public class VoucherServiceImpl 
    extends ServiceImpl<VoucherMapper, Voucher> 
    implements IVoucherService {

    @Autowired
    private VoucherDetailMapper voucherDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> addVoucher(Voucher voucher, List<VoucherDetail> details) {
        // 验证借贷平衡
        if (!validateDebitCreditBalance(details)) {
            return R.fail("借贷不平衡，无法保存凭证");
        }

        // 设置凭证基本信息
        voucher.setMakerId(SecurityUtils.getUserId());
        voucher.setMakerName(SecurityUtils.getUsername());
        voucher.setMakerTime(new Date());
        voucher.setStatus("DRAFT");
        voucher.setTotalAmount(calculateTotalAmount(details));

        // 生成凭证号
        String voucherNo = generateVoucherNo(voucher.getPeriod());
        voucher.setVoucherNo(voucherNo);

        // 保存凭证主表
        save(voucher);

        // 保存凭证明细
        for (int i = 0; i < details.size(); i++) {
            VoucherDetail detail = details.get(i);
            detail.setVoucherId(voucher.getVoucherId());
            detail.setEntryNo(i + 1);
            detail.setCreateTime(new Date());
            voucherDetailMapper.insert(detail);
        }

        return R.success("凭证保存成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateVoucher(Voucher voucher, List<VoucherDetail> details) {
        if (!validateDebitCreditBalance(details)) {
            return R.fail("借贷不平衡，无法修改凭证");
        }

        // 删除原有明细
        voucherDetailMapper.delete(new LambdaQueryWrapper<VoucherDetail>()
            .eq(VoucherDetail::getVoucherId, voucher.getVoucherId()));

        // 更新凭证主表
        voucher.setTotalAmount(calculateTotalAmount(details));
        voucher.setUpdateTime(new Date());
        updateById(voucher);

        // 保存新明细
        for (int i = 0; i < details.size(); i++) {
            VoucherDetail detail = details.get(i);
            detail.setVoucherId(voucher.getVoucherId());
            detail.setEntryNo(i + 1);
            detail.setCreateTime(new Date());
            voucherDetailMapper.insert(detail);
        }

        return R.success("凭证修改成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> deleteVoucher(Long voucherId) {
        // 删除明细
        voucherDetailMapper.delete(new LambdaQueryWrapper<VoucherDetail>()
            .eq(VoucherDetail::getVoucherId, voucherId));
        
        // 删除主表
        removeById(voucherId);

        return R.success("凭证删除成功");
    }

    @Override
    public R<?> auditVoucher(Long voucherId, String auditorName) {
        Voucher voucher = getById(voucherId);
        if (voucher == null) {
            return R.fail("凭证不存在");
        }
        if (!"DRAFT".equals(voucher.getStatus())) {
            return R.fail("凭证状态不正确，无法审核");
        }

        voucher.setStatus("AUDITED");
        voucher.setAuditorId(SecurityUtils.getUserId());
        voucher.setAuditorName(auditorName);
        voucher.setAuditorTime(new Date());
        voucher.setUpdateTime(new Date());

        updateById(voucher);
        return R.success("凭证审核成功");
    }

    @Override
    public R<?> postVoucher(Long voucherId, String posterName) {
        Voucher voucher = getById(voucherId);
        if (voucher == null) {
            return R.fail("凭证不存在");
        }
        if (!"AUDITED".equals(voucher.getStatus())) {
            return R.fail("凭证未审核，无法记账");
        }

        voucher.setStatus("POSTED");
        voucher.setPosterId(SecurityUtils.getUserId());
        voucher.setPosterName(posterName);
        voucher.setPosterTime(new Date());
        voucher.setUpdateTime(new Date());

        updateById(voucher);
        return R.success("凭证记账成功");
    }

    @Override
    public R<Voucher> getVoucherWithDetails(Long voucherId) {
        Voucher voucher = getById(voucherId);
        if (voucher == null) {
            return R.fail("凭证不存在");
        }

        // 查询凭证明细（如果需要返回明细，可以在这里处理）
        // List<VoucherDetail> details = voucherDetailMapper.selectList(
        //     new LambdaQueryWrapper<VoucherDetail>()
        //         .eq(VoucherDetail::getVoucherId, voucherId)
        //         .orderByAsc(VoucherDetail::getEntryNo)
        // );

        // 这里可以使用VO对象返回，简化处理直接返回Voucher对象
        return R.success(voucher);
    }

    @Override
    public String generateVoucherNo(String period) {
        LambdaQueryWrapper<Voucher> wrapper = new LambdaQueryWrapper<>();
        wrapper.likeRight(Voucher::getVoucherNo, period)
               .orderByDesc(Voucher::getVoucherNo)
               .last("LIMIT 1");

        Voucher lastVoucher = getOne(wrapper);
        if (lastVoucher == null) {
            return period + "0001";
        }

        String lastNo = lastVoucher.getVoucherNo();
        int sequence = Integer.parseInt(lastNo.substring(6)) + 1;
        return period + String.format("%04d", sequence);
    }

    /**
     * 验证借贷平衡
     */
    private boolean validateDebitCreditBalance(List<VoucherDetail> details) {
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;

        for (VoucherDetail detail : details) {
            if ("1".equals(detail.getDirection())) {
                debitTotal = debitTotal.add(detail.getAmount());
            } else if ("-1".equals(detail.getDirection())) {
                creditTotal = creditTotal.add(detail.getAmount());
            }
        }

        return debitTotal.compareTo(creditTotal) == 0;
    }

    /**
     * 计算合计金额
     */
    private BigDecimal calculateTotalAmount(List<VoucherDetail> details) {
        return details.stream()
            .map(VoucherDetail::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}