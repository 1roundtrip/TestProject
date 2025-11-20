package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.FinanceCustomer;
import com.coal.erp.common.core.domain.R;

/**
 * 客户档案服务接口
 */
public interface IFinanceCustomerService extends IService<FinanceCustomer> {
    
    /**
     * 创建客户档案
     */
    R<?> createCustomer(FinanceCustomer customer);
    
    /**
     * 更新客户档案
     */
    R<?> updateCustomer(FinanceCustomer customer);
    
    /**
     * 检查客户编码是否唯一
     */
    boolean checkCustomerCodeUnique(FinanceCustomer customer);
    
    /**
     * 获取客户信用信息
     */
    R<?> getCustomerCreditInfo(Long customerId);
    
    /**
     * 获取电厂客户列表
     */
    R<?> getElectricPlantCustomers();
    
    /**
     * 获取钢厂客户列表
     */
    R<?> getSteelPlantCustomers();
    
    /**
     * 获取贸易商客户列表
     */
    R<?> getTraderCustomers();
}