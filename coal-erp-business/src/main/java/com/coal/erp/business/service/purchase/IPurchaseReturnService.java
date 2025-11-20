package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchaseReturn;
import com.coal.erp.business.domain.purchase.PurchaseReturnDetail;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 采购退货服务接口
 */
public interface IPurchaseReturnService extends IService<PurchaseReturn> {
    
    /**
     * 创建退货单
     */
    R<?> createReturn(PurchaseReturn returnOrder, List<PurchaseReturnDetail> details);
    
    /**
     * 提交审批
     */
    R<?> submitReturn(Long returnId);
    
    /**
     * 审批通过
     */
    R<?> approveReturn(Long returnId, String approveRemark);
    
    /**
     * 确认退货
     */
    R<?> confirmReturn(Long returnId);
    
    /**
     * 分页查询
     */
    Page<PurchaseReturn> pageReturn(Long current, Long size, String returnNo, String status);
    
    /**
     * 获取退货明细
     */
    List<PurchaseReturnDetail> getReturnDetails(Long returnId);
}

