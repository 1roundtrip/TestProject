package com.coal.erp.business.controller.finance;

import com.coal.erp.business.service.finance.IPeriodEndService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 期末处理控制器
 */
@RestController
@RequestMapping("/api/finance/period")
public class PeriodEndController {

    @Autowired
    private IPeriodEndService periodEndService;

    /**
     * 自动转账
     */
    @PostMapping("/autoTransfer")
    public R<?> autoTransfer(@RequestParam String period) {
        return periodEndService.autoTransfer(period);
    }

    /**
     * 期末调汇
     */
    @PostMapping("/exchangeAdjustment")
    public R<?> exchangeAdjustment(@RequestParam String period) {
        return periodEndService.exchangeAdjustment(period);
    }

    /**
     * 结转损益
     */
    @PostMapping("/transferProfitLoss")
    public R<?> transferProfitLoss(@RequestParam String period) {
        return periodEndService.transferProfitLoss(period);
    }

    /**
     * 期末结账
     */
    @PostMapping("/closing")
    public R<?> periodClosing(@RequestParam String period) {
        return periodEndService.periodClosing(period);
    }

    /**
     * 反结账
     */
    @PostMapping("/reverseClosing")
    public R<?> reverseClosing(@RequestParam String period) {
        return periodEndService.reverseClosing(period);
    }
}