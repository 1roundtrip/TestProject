package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.finance.FinanceSupplier;
import com.coal.erp.business.service.finance.IFinanceSupplierService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 供应商档案控制器
 */
@RestController
@RequestMapping("/finance/supplier")
public class FinanceSupplierController {

    @Autowired
    private IFinanceSupplierService financeSupplierService;

    /**
     * 分页查询供应商列表
     */
    @GetMapping("/page")
    public R<Page<FinanceSupplier>> page(@RequestParam(defaultValue = "1") Long current,
                                        @RequestParam(defaultValue = "10") Long size,
                                        @RequestParam(required = false) String supplierName,
                                        @RequestParam(required = false) String supplierType) {
        Page<FinanceSupplier> page = new Page<>(current, size);
        LambdaQueryWrapper<FinanceSupplier> wrapper = new LambdaQueryWrapper<>();
        
        if (supplierName != null && !supplierName.isEmpty()) {
            wrapper.like(FinanceSupplier::getSupplierName, supplierName);
        }
        if (supplierType != null && !supplierType.isEmpty()) {
            wrapper.eq(FinanceSupplier::getSupplierType, supplierType);
        }
        wrapper.eq(FinanceSupplier::getStatus, "0");
        wrapper.orderByAsc(FinanceSupplier::getSupplierCode);

        return R.success(financeSupplierService.page(page, wrapper));
    }

    /**
     * 获取供应商详情
     */
    @GetMapping("/{supplierId}")
    public R<FinanceSupplier> getById(@PathVariable Long supplierId) {
        FinanceSupplier supplier = financeSupplierService.getById(supplierId);
        return supplier != null ? R.success(supplier) : R.fail("供应商不存在");
    }

    /**
     * 创建供应商档案
     */
    @PostMapping
    public R<?> create(@RequestBody FinanceSupplier supplier) {
        return financeSupplierService.createSupplier(supplier);
    }

    /**
     * 更新供应商档案
     */
    @PutMapping
    public R<?> update(@RequestBody FinanceSupplier supplier) {
        return financeSupplierService.updateSupplier(supplier);
    }

    /**
     * 删除供应商档案
     */
    @DeleteMapping("/{supplierId}")
    public R<?> delete(@PathVariable Long supplierId) {
        FinanceSupplier supplier = financeSupplierService.getById(supplierId);
        if (supplier == null) {
            return R.fail("供应商不存在");
        }
        supplier.setStatus("1"); // 停用
        boolean success = financeSupplierService.updateById(supplier);
        return success ? R.success("供应商删除成功") : R.fail("供应商删除失败");
    }

    /**
     * 获取设备供应商列表
     */
    @GetMapping("/equipment")
    public R<?> getEquipmentSuppliers() {
        return financeSupplierService.getEquipmentSuppliers();
    }

    /**
     * 获取服务供应商列表
     */
    @GetMapping("/service")
    public R<?> getServiceSuppliers() {
        return financeSupplierService.getServiceSuppliers();
    }

    /**
     * 获取材料供应商列表
     */
    @GetMapping("/material")
    public R<?> getMaterialSuppliers() {
        return financeSupplierService.getMaterialSuppliers();
    }

    /**
     * 评估供应商绩效
     */
    @PostMapping("/evaluate/{supplierId}")
    public R<?> evaluateSupplier(@PathVariable Long supplierId) {
        return financeSupplierService.evaluateSupplierPerformance(supplierId);
    }

    /**
     * 获取供应商类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getSupplierTypes() {
        List<String> types = Arrays.asList(
            "EQUIPMENT",  // 设备供应商
            "SERVICE",    // 服务供应商
            "MATERIAL"    // 材料供应商
        );
        return R.success(types);
    }
}