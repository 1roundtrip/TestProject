package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.finance.FinanceCustomer;
import com.coal.erp.business.service.finance.IFinanceCustomerService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 客户档案控制器
 */
@RestController
@RequestMapping("/finance/customer")
public class FinanceCustomerController {

    @Autowired
    private IFinanceCustomerService financeCustomerService;

    /**
     * 分页查询客户列表
     */
    @GetMapping("/page")
    public R<Page<FinanceCustomer>> page(@RequestParam(defaultValue = "1") Long current,
                                        @RequestParam(defaultValue = "10") Long size,
                                        @RequestParam(required = false) String customerName,
                                        @RequestParam(required = false) String customerType) {
        Page<FinanceCustomer> page = new Page<>(current, size);
        LambdaQueryWrapper<FinanceCustomer> wrapper = new LambdaQueryWrapper<>();
        
        if (customerName != null && !customerName.isEmpty()) {
            wrapper.like(FinanceCustomer::getCustomerName, customerName);
        }
        if (customerType != null && !customerType.isEmpty()) {
            wrapper.eq(FinanceCustomer::getCustomerType, customerType);
        }
        wrapper.eq(FinanceCustomer::getStatus, "0");
        wrapper.orderByAsc(FinanceCustomer::getCustomerCode);

        return R.success(financeCustomerService.page(page, wrapper));
    }

    /**
     * 获取客户详情
     */
    @GetMapping("/{customerId}")
    public R<FinanceCustomer> getById(@PathVariable Long customerId) {
        FinanceCustomer customer = financeCustomerService.getById(customerId);
        return customer != null ? R.success(customer) : R.fail("客户不存在");
    }

    /**
     * 创建客户档案
     */
    @PostMapping
    public R<?> create(@RequestBody FinanceCustomer customer) {
        return financeCustomerService.createCustomer(customer);
    }

    /**
     * 更新客户档案
     */
    @PutMapping
    public R<?> update(@RequestBody FinanceCustomer customer) {
        return financeCustomerService.updateCustomer(customer);
    }

    /**
     * 删除客户档案
     */
    @DeleteMapping("/{customerId}")
    public R<?> delete(@PathVariable Long customerId) {
        FinanceCustomer customer = financeCustomerService.getById(customerId);
        if (customer == null) {
            return R.fail("客户不存在");
        }
        customer.setStatus("1"); // 停用
        boolean success = financeCustomerService.updateById(customer);
        return success ? R.success("客户删除成功") : R.fail("客户删除失败");
    }

    /**
     * 获取客户信用信息
     */
    @GetMapping("/credit/{customerId}")
    public R<?> getCreditInfo(@PathVariable Long customerId) {
        return financeCustomerService.getCustomerCreditInfo(customerId);
    }

    /**
     * 获取电厂客户列表
     */
    @GetMapping("/electric-plant")
    public R<?> getElectricPlantCustomers() {
        return financeCustomerService.getElectricPlantCustomers();
    }

    /**
     * 获取钢厂客户列表
     */
    @GetMapping("/steel-plant")
    public R<?> getSteelPlantCustomers() {
        return financeCustomerService.getSteelPlantCustomers();
    }

    /**
     * 获取贸易商客户列表
     */
    @GetMapping("/trader")
    public R<?> getTraderCustomers() {
        return financeCustomerService.getTraderCustomers();
    }

    /**
     * 获取客户类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getCustomerTypes() {
        List<String> types = Arrays.asList(
            "ELECTRIC_PLANT",  // 电厂
            "STEEL_PLANT",     // 钢厂
            "TRADER"           // 贸易商
        );
        return R.success(types);
    }
}