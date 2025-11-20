package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.Voucher;
import com.coal.erp.business.domain.finance.VoucherDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 凭证服务接口
 */
public interface IVoucherService extends IService<Voucher> {
    /**
     * 新增凭证
     */
    R<?> addVoucher(Voucher voucher, List<VoucherDetail> details);
    
    /**
     * 修改凭证
     */
    R<?> updateVoucher(Voucher voucher, List<VoucherDetail> details);
    
    /**
     * 删除凭证
     */
    R<?> deleteVoucher(Long voucherId);
    
    /**
     * 审核凭证
     */
    R<?> auditVoucher(Long voucherId, String auditorName);
    
    /**
     * 记账凭证
     */
    R<?> postVoucher(Long voucherId, String posterName);
    
    /**
     * 获取凭证详情（包含明细）
     */
    R<Voucher> getVoucherWithDetails(Long voucherId);
    
    /**
     * 生成凭证号
     */
    String generateVoucherNo(String period);
}