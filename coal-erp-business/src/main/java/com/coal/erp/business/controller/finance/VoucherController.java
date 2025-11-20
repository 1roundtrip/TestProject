package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.FinanceSecurityConfig;
import com.coal.erp.business.domain.finance.Voucher;
import com.coal.erp.business.service.finance.IVoucherService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 凭证控制器
 */
@RestController
@RequestMapping("/api/finance/voucher")
@FinanceSecurityConfig.RequiresVoucherPermission
public class VoucherController {

    @Autowired
    private IVoucherService voucherService;

    /**
     * 分页查询凭证
     */
    @GetMapping("/page")
    public R<Page<Voucher>> page(@RequestParam(defaultValue = "1") Long current,
                                 @RequestParam(defaultValue = "10") Long size,
                                 @RequestParam(required = false) String voucherNo,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String period) {
        Page<Voucher> page = new Page<>(current, size);
        LambdaQueryWrapper<Voucher> wrapper = new LambdaQueryWrapper<>();
        
        if (voucherNo != null && !voucherNo.isEmpty()) {
            wrapper.like(Voucher::getVoucherNo, voucherNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Voucher::getStatus, status);
        }
        if (period != null && !period.isEmpty()) {
            wrapper.eq(Voucher::getPeriod, period);
        }
        wrapper.orderByDesc(Voucher::getVoucherDate, Voucher::getVoucherNo);

        return R.success(voucherService.page(page, wrapper));
    }

    /**
     * 获取凭证详情
     */
    @GetMapping("/{voucherId}")
    public R<Voucher> getById(@PathVariable Long voucherId) {
        return voucherService.getVoucherWithDetails(voucherId);
    }

    /**
     * 新增凭证
     */
    @PostMapping
    @FinanceSecurityConfig.RequiresVoucherPermission("add")
    public R<?> add(@RequestBody Voucher voucher) {
        return voucherService.addVoucher(voucher, voucher.getDetails());
    }

    /**
     * 修改凭证
     */
    @PutMapping
    @FinanceSecurityConfig.RequiresVoucherPermission("edit")
    public R<?> update(@RequestBody Voucher voucher) {
        return voucherService.updateVoucher(voucher, voucher.getDetails());
    }

    /**
     * 删除凭证
     */
    @DeleteMapping("/{voucherId}")
    @FinanceSecurityConfig.RequiresVoucherPermission("remove")
    public R<?> delete(@PathVariable Long voucherId) {
        return voucherService.deleteVoucher(voucherId);
    }

    /**
     * 审核凭证
     */
    @PostMapping("/{voucherId}/audit")
    @FinanceSecurityConfig.RequiresVoucherPermission("audit")
    public R<?> audit(@PathVariable Long voucherId,
                     @RequestParam String auditorName) {
        return voucherService.auditVoucher(voucherId, auditorName);
    }

    /**
     * 记账凭证
     */
    @PostMapping("/{voucherId}/post")
    @FinanceSecurityConfig.RequiresVoucherPermission("post")
    public R<?> post(@PathVariable Long voucherId,
                    @RequestParam String posterName) {
        return voucherService.postVoucher(voucherId, posterName);
    }

    /**
     * 生成凭证号
     */
    @GetMapping("/generateNo")
    public R<String> generateVoucherNo(@RequestParam String period) {
        return R.success(voucherService.generateVoucherNo(period));
    }

    /**
     * 获取凭证状态枚举
     */
    @GetMapping("/statuses")
    public R<List<String>> getVoucherStatuses() {
        List<String> statuses = Arrays.asList("DRAFT", "AUDITED", "POSTED");
        return R.success(statuses);
    }
}