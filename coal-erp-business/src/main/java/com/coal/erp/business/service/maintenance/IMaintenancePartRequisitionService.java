package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenancePartRequisition;
import com.coal.erp.business.domain.maintenance.MaintenancePartRequisitionDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 维修备件领用服务接口
 */
public interface IMaintenancePartRequisitionService extends IService<MaintenancePartRequisition> {
    
    /**
     * 创建领用单
     */
    R<?> createRequisition(MaintenancePartRequisition requisition, List<MaintenancePartRequisitionDetail> details);
    
    /**
     * 分页查询
     */
    Page<MaintenancePartRequisition> pageRequisition(Long current, Long size, String requisitionNo, String status);
    
    /**
     * 审批领用单
     */
    R<?> approveRequisition(Long requisitionId);
    
    /**
     * 发放备件
     */
    R<?> issueRequisition(Long requisitionId);
    
    /**
     * 获取领用明细
     */
    List<MaintenancePartRequisitionDetail> getRequisitionDetails(Long requisitionId);
}

