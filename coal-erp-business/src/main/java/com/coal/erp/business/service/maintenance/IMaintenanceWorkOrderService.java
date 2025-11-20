package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrder;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrderDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 维修工单服务接口
 */
public interface IMaintenanceWorkOrderService extends IService<MaintenanceWorkOrder> {
    
    /**
     * 创建维修工单
     */
    R<?> createWorkOrder(MaintenanceWorkOrder workOrder, List<MaintenanceWorkOrderDetail> details);
    
    /**
     * 分页查询工单
     */
    Page<MaintenanceWorkOrder> pageWorkOrder(Long current, Long size, String workOrderNo, String status, Long assetId);
    
    /**
     * 分配工单
     */
    R<?> assignWorkOrder(Long workOrderId, Long teamId, Long technicianId);
    
    /**
     * 开始维修
     */
    R<?> startWorkOrder(Long workOrderId);
    
    /**
     * 完成工单
     */
    R<?> completeWorkOrder(Long workOrderId, String qualityComment, java.math.BigDecimal qualityScore);
    
    /**
     * 获取工单明细
     */
    List<MaintenanceWorkOrderDetail> getWorkOrderDetails(Long workOrderId);
    
    /**
     * 更新工单明细
     */
    R<?> updateWorkOrderDetails(Long workOrderId, List<MaintenanceWorkOrderDetail> details);
}

