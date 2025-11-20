package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchaseRequisition;
import com.coal.erp.business.domain.purchase.PurchaseRequisitionDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购申请服务接口
 */
public interface IPurchaseRequisitionService extends IService<PurchaseRequisition> {
    
    /**
     * 创建采购申请
     */
    R<?> createRequisition(PurchaseRequisition requisition, List<PurchaseRequisitionDetail> details);
    
    /**
     * 提交审批
     */
    R<?> submitRequisition(Long requisitionId);
    
    /**
     * 审批通过
     */
    R<?> approveRequisition(Long requisitionId, String approveRemark);
    
    /**
     * 审批驳回
     */
    R<?> rejectRequisition(Long requisitionId, String approveRemark);
    
    /**
     * 分页查询
     */
    Page<PurchaseRequisition> pageRequisition(Long current, Long size, String requisitionNo, String status);
    
    /**
     * 获取申请明细
     */
    List<PurchaseRequisitionDetail> getRequisitionDetails(Long requisitionId);
}

