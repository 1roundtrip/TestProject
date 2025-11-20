package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.FinanceCustomer;
import com.coal.erp.business.mapper.finance.FinanceCustomerMapper;
import com.coal.erp.business.service.finance.IFinanceCustomerService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户档案服务实现
 */
@Service
public class FinanceCustomerServiceImpl 
    extends ServiceImpl<FinanceCustomerMapper, FinanceCustomer> 
    implements IFinanceCustomerService {

    @Override
    public R<?> createCustomer(FinanceCustomer customer) {
        if (!checkCustomerCodeUnique(customer)) {
            return R.fail("客户编码已存在");
        }
        
        // 设置默认值
        if (customer.getCreditAmount() == null) {
            customer.setCreditAmount(new java.math.BigDecimal("0.00"));
        }
        if (customer.getStatus() == null) {
            customer.setStatus("0");
        }
        
        boolean success = save(customer);
        return success ? R.success(customer) : R.fail("创建客户失败");
    }

    @Override
    public R<?> updateCustomer(FinanceCustomer customer) {
        if (!checkCustomerCodeUnique(customer)) {
            return R.fail("客户编码已存在");
        }
        
        boolean success = updateById(customer);
        return success ? R.success(customer) : R.fail("更新客户失败");
    }

    @Override
    public boolean checkCustomerCodeUnique(FinanceCustomer customer) {
        Long customerId = customer.getCustomerId() == null ? -1L : customer.getCustomerId();
        FinanceCustomer existing = lambdaQuery()
            .eq(FinanceCustomer::getCustomerCode, customer.getCustomerCode())
            .one();
        return existing == null || existing.getCustomerId().equals(customerId);
    }

    @Override
    public R<?> getCustomerCreditInfo(Long customerId) {
        FinanceCustomer customer = getById(customerId);
        if (customer == null) {
            return R.fail("客户不存在");
        }
        
        // TODO: 计算客户当前应收余额和信用使用情况
        return R.success(customer);
    }

    @Override
    public R<?> getElectricPlantCustomers() {
        List<FinanceCustomer> customers = lambdaQuery()
            .eq(FinanceCustomer::getCustomerType, "ELECTRIC_PLANT")
            .eq(FinanceCustomer::getStatus, "0")
            .list();
        return R.success(customers);
    }

    @Override
    public R<?> getSteelPlantCustomers() {
        List<FinanceCustomer> customers = lambdaQuery()
            .eq(FinanceCustomer::getCustomerType, "STEEL_PLANT")
            .eq(FinanceCustomer::getStatus, "0")
            .list();
        return R.success(customers);
    }

    @Override
    public R<?> getTraderCustomers() {
        List<FinanceCustomer> customers = lambdaQuery()
            .eq(FinanceCustomer::getCustomerType, "TRADER")
            .eq(FinanceCustomer::getStatus, "0")
            .list();
        return R.success(customers);
    }
}