package com.coal.erp.business.service.integration;

import com.coal.erp.common.core.domain.R;

/**
 * 业务流程集成服务接口
 */
public interface IBusinessIntegrationService {
    
    /**
     * 采购到资产流程集成
     * 采购订单 → 收货入库 → 资产建档 → 财务付款
     */
    R<?> integratePurchaseToAsset(Long receivingId);
    
    /**
     * 维修业务流集成
     * 设备预警 → 维修工单 → 备件领用 → 维修执行 → 费用核算
     */
    R<?> integrateMaintenanceBusiness(Long warningRecordId, Long workOrderId);
    
    /**
     * 库存管理流集成
     * 安全库存预警 → 采购申请 → 库存补充 → 资产领用
     */
    R<?> integrateInventoryManagement(Long warningId);
    
    /**
     * 预警处理流集成
     * 规则监控 → 预警触发 → 通知分发 → 处理跟踪 → 结果反馈
     */
    R<?> integrateWarningProcess(Long recordId);
    
    /**
     * 同步业务流程状态
     */
    R<?> syncBusinessStatus(String businessType, Long businessId, String status);
    
    /**
     * 处理业务异常回滚
     */
    R<?> rollbackBusiness(String businessType, Long businessId, String reason);
}

