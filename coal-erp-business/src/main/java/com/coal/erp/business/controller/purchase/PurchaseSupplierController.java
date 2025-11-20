package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchaseSupplier;
import com.coal.erp.business.domain.purchase.PurchaseSupplierEvaluation;
import com.coal.erp.business.service.purchase.IPurchaseSupplierService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商管理控制器
 */
@RestController
@RequestMapping("/api/purchase/supplier")
public class PurchaseSupplierController {
    
    @Autowired
    private IPurchaseSupplierService supplierService;
    
    /**
     * 分页查询供应商
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:supplier:list')")
    public R<Page<PurchaseSupplier>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String supplierName,
            @RequestParam(required = false) String status) {
        return R.success(supplierService.pageSupplier(current, size, supplierName, status));
    }
    
    /**
     * 新增供应商
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:supplier:add')")
    public R<?> add(@RequestBody PurchaseSupplier supplier) {
        return R.success(supplierService.save(supplier));
    }
    
    /**
     * 更新供应商
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:supplier:edit')")
    public R<?> update(@RequestBody PurchaseSupplier supplier) {
        return R.success(supplierService.updateById(supplier));
    }
    
    /**
     * 删除供应商
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:supplier:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(supplierService.removeById(id));
    }
    
    /**
     * 评价供应商
     */
    @PostMapping("/{id}/evaluate")
    @PreAuthorize("hasPermission(null, 'purchase:supplier:evaluate')")
    public R<?> evaluate(@PathVariable Long id, @RequestBody PurchaseSupplierEvaluation evaluation) {
        evaluation.setSupplierId(id);
        return supplierService.evaluateSupplier(evaluation);
    }
    
    /**
     * 获取供应商评价记录
     */
    @GetMapping("/{id}/evaluations")
    @PreAuthorize("hasPermission(null, 'purchase:supplier:list')")
    public R<List<PurchaseSupplierEvaluation>> getEvaluations(@PathVariable Long id) {
        return R.success(supplierService.getSupplierEvaluations(id));
    }
    
    /**
     * 更新供应商评分
     */
    @PostMapping("/{id}/update-rating")
    @PreAuthorize("hasPermission(null, 'purchase:supplier:edit')")
    public R<?> updateRating(@PathVariable Long id) {
        return supplierService.updateSupplierRating(id);
    }
}

