package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.FinanceSupplier;
import com.coal.erp.common.core.domain.R;

/**
 * 供应商档案服务接口
 */
public interface IFinanceSupplierService extends IService<FinanceSupplier> {
    
    /**
     * 创建供应商档案
     */
    R<?> createSupplier(FinanceSupplier supplier);
    
    /**
     * 更新供应商档案
     */
    R<?> updateSupplier(FinanceSupplier supplier);
    
    /**
     * 检查供应商编码是否唯一
     */
    boolean checkSupplierCodeUnique(FinanceSupplier supplier);
    
    /**
     * 获取设备供应商列表
     */
    R<?> getEquipmentSuppliers();
    
    /**
     * 获取服务供应商列表
     */
    R<?> getServiceSuppliers();
    
    /**
     * 获取材料供应商列表
     */
    R<?> getMaterialSuppliers();
    
    /**
     * 评估供应商绩效
     */
    R<?> evaluateSupplierPerformance(Long supplierId);
}