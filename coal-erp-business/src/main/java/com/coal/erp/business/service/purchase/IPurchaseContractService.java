package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchaseContract;
import com.coal.erp.business.domain.purchase.PurchaseContractDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购合同服务接口
 */
public interface IPurchaseContractService extends IService<PurchaseContract> {
    
    /**
     * 创建采购合同
     */
    R<?> createContract(PurchaseContract contract, List<PurchaseContractDetail> details);
    
    /**
     * 提交审批
     */
    R<?> submitContract(Long contractId);
    
    /**
     * 审批通过
     */
    R<?> approveContract(Long contractId, String approveRemark);
    
    /**
     * 签订合同
     */
    R<?> signContract(Long contractId);
    
    /**
     * 分页查询
     */
    Page<PurchaseContract> pageContract(Long current, Long size, String contractNo, String status);
    
    /**
     * 获取合同明细
     */
    List<PurchaseContractDetail> getContractDetails(Long contractId);
    
    /**
     * 从采购订单创建合同
     */
    R<?> createContractFromOrder(Long orderId);
}

