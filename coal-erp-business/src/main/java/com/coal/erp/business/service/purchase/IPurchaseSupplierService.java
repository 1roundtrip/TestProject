package com.coal.erp.business.service.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.purchase.PurchaseSupplier;
import com.coal.erp.business.domain.purchase.PurchaseSupplierEvaluation;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 供应商服务接口
 */
public interface IPurchaseSupplierService extends IService<PurchaseSupplier> {
    
    /**
     * 分页查询供应商
     */
    Page<PurchaseSupplier> pageSupplier(Long current, Long size, String supplierName, String status);
    
    /**
     * 评价供应商
     */
    R<?> evaluateSupplier(PurchaseSupplierEvaluation evaluation);
    
    /**
     * 获取供应商评价记录
     */
    List<PurchaseSupplierEvaluation> getSupplierEvaluations(Long supplierId);
    
    /**
     * 更新供应商评分
     */
    R<?> updateSupplierRating(Long supplierId);
}

