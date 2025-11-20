package com.coal.erp.business.controller.finance;

import com.coal.erp.business.domain.finance.AccountBalance;
import com.coal.erp.business.domain.finance.VoucherDetail;
import com.coal.erp.business.service.finance.IAccountBookService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 账簿查询控制器
 */
@RestController
@RequestMapping("/api/finance/book")
public class AccountBookController {

    @Autowired
    private IAccountBookService accountBookService;

    /**
     * 查询总账
     */
    @GetMapping("/general")
    public R<List<AccountBalance>> getGeneralLedger(@RequestParam String period) {
        return accountBookService.getGeneralLedger(period);
    }

    /**
     * 查询明细账
     */
    @GetMapping("/detail")
    public R<List<VoucherDetail>> getDetailLedger(@RequestParam Long subjectId,
                                                 @RequestParam String period) {
        return accountBookService.getDetailLedger(subjectId, period);
    }

    /**
     * 查询多栏账
     */
    @GetMapping("/multiColumn")
    public R<Map<String, Object>> getMultiColumnLedger(@RequestParam Long subjectId,
                                                      @RequestParam String period) {
        return accountBookService.getMultiColumnLedger(subjectId, period);
    }

    /**
     * 查询余额表
     */
    @GetMapping("/balance")
    public R<List<AccountBalance>> getBalanceSheet(@RequestParam String period) {
        return accountBookService.getBalanceSheet(period);
    }

    /**
     * 更新科目余额
     */
    @PostMapping("/updateBalance")
    public R<?> updateAccountBalance(@RequestParam String period) {
        accountBookService.updateAccountBalance(period);
        return R.success("科目余额更新成功");
    }

    /**
     * 计算科目余额
     */
    @GetMapping("/calculateBalance")
    public R<String> calculateSubjectBalance(@RequestParam Long subjectId,
                                            @RequestParam String period) {
        return R.success(accountBookService.calculateSubjectBalance(subjectId, period).toString());
    }
}