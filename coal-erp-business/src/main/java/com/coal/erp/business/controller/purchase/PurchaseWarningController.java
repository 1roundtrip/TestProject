package com.coal.erp.business.controller.purchase;

import com.coal.erp.business.service.purchase.IPurchaseWarningService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 采购预警管理控制器
 */
@RestController
@RequestMapping("/api/purchase/warning")
public class PurchaseWarningController {
    
    @Autowired
    private IPurchaseWarningService warningService;
    
    /**
     * 检查订单超期预警
     */
    @PostMapping("/check-order-overdue")
    @PreAuthorize("hasPermission(null, 'purchase:report:view')")
    public R<?> checkOrderOverdue() {
        return warningService.checkOrderOverdue();
    }
    
    /**
     * 检查付款超期预警
     */
    @PostMapping("/check-payment-overdue")
    @PreAuthorize("hasPermission(null, 'purchase:report:view')")
    public R<?> checkPaymentOverdue() {
        return warningService.checkPaymentOverdue();
    }
    
    /**
     * 检查质量问题预警
     */
    @PostMapping("/check-quality-issue")
    @PreAuthorize("hasPermission(null, 'purchase:report:view')")
    public R<?> checkQualityIssue() {
        return warningService.checkQualityIssue();
    }
    
    /**
     * 检查供应商风险预警
     */
    @PostMapping("/check-supplier-risk")
    @PreAuthorize("hasPermission(null, 'purchase:report:view')")
    public R<?> checkSupplierRisk() {
        return warningService.checkSupplierRisk();
    }
}

