package com.coal.erp.business.controller.integration;

import com.coal.erp.business.service.integration.IBusinessIntegrationService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 业务流程集成控制器
 */
@RestController
@RequestMapping("/api/integration")
public class BusinessIntegrationController {
    
    @Autowired
    private IBusinessIntegrationService integrationService;
    
    /**
     * 采购到资产流程集成
     */
    @PostMapping("/purchase-to-asset/{receivingId}")
    @PreAuthorize("hasPermission(null, 'integration:purchase-to-asset')")
    public R<?> integratePurchaseToAsset(@PathVariable Long receivingId) {
        return integrationService.integratePurchaseToAsset(receivingId);
    }
    
    /**
     * 维修业务流集成
     */
    @PostMapping("/maintenance-business")
    @PreAuthorize("hasPermission(null, 'integration:maintenance-business')")
    public R<?> integrateMaintenanceBusiness(
            @RequestParam(required = false) Long warningRecordId,
            @RequestParam Long workOrderId) {
        return integrationService.integrateMaintenanceBusiness(warningRecordId, workOrderId);
    }
    
    /**
     * 库存管理流集成
     */
    @PostMapping("/inventory-management/{warningId}")
    @PreAuthorize("hasPermission(null, 'integration:inventory-management')")
    public R<?> integrateInventoryManagement(@PathVariable Long warningId) {
        return integrationService.integrateInventoryManagement(warningId);
    }
    
    /**
     * 预警处理流集成
     */
    @PostMapping("/warning-process/{recordId}")
    @PreAuthorize("hasPermission(null, 'integration:warning-process')")
    public R<?> integrateWarningProcess(@PathVariable Long recordId) {
        return integrationService.integrateWarningProcess(recordId);
    }
    
    /**
     * 同步业务流程状态
     */
    @PostMapping("/sync-status")
    @PreAuthorize("hasPermission(null, 'integration:sync-status')")
    public R<?> syncBusinessStatus(
            @RequestParam String businessType,
            @RequestParam Long businessId,
            @RequestParam String status) {
        return integrationService.syncBusinessStatus(businessType, businessId, status);
    }
    
    /**
     * 业务回滚
     */
    @PostMapping("/rollback")
    @PreAuthorize("hasPermission(null, 'integration:rollback')")
    public R<?> rollbackBusiness(
            @RequestParam String businessType,
            @RequestParam Long businessId,
            @RequestParam String reason) {
        return integrationService.rollbackBusiness(businessType, businessId, reason);
    }
}

