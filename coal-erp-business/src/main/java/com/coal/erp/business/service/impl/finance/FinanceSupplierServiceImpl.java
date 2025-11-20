package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.FinanceSupplier;
import com.coal.erp.business.mapper.finance.FinanceSupplierMapper;
import com.coal.erp.business.service.finance.IFinanceSupplierService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商档案服务实现
 */
@Service
public class FinanceSupplierServiceImpl 
    extends ServiceImpl<FinanceSupplierMapper, FinanceSupplier> 
    implements IFinanceSupplierService {

    @Override
    public R<?> createSupplier(FinanceSupplier supplier) {
        if (!checkSupplierCodeUnique(supplier)) {
            return R.fail("供应商编码已存在");
        }
        
        // 设置默认值
        if (supplier.getStatus() == null) {
            supplier.setStatus("0");
        }
        
        boolean success = save(supplier);
        return success ? R.success(supplier) : R.fail("创建供应商失败");
    }

    @Override
    public R<?> updateSupplier(FinanceSupplier supplier) {
        if (!checkSupplierCodeUnique(supplier)) {
            return R.fail("供应商编码已存在");
        }
        
        boolean success = updateById(supplier);
        return success ? R.success(supplier) : R.fail("更新供应商失败");
    }

    @Override
    public boolean checkSupplierCodeUnique(FinanceSupplier supplier) {
        Long supplierId = supplier.getSupplierId() == null ? -1L : supplier.getSupplierId();
        FinanceSupplier existing = lambdaQuery()
            .eq(FinanceSupplier::getSupplierCode, supplier.getSupplierCode())
            .one();
        return existing == null || existing.getSupplierId().equals(supplierId);
    }

    @Override
    public R<?> getEquipmentSuppliers() {
        List<FinanceSupplier> suppliers = lambdaQuery()
            .eq(FinanceSupplier::getSupplierType, "EQUIPMENT")
            .eq(FinanceSupplier::getStatus, "0")
            .list();
        return R.success(suppliers);
    }

    @Override
    public R<?> getServiceSuppliers() {
        List<FinanceSupplier> suppliers = lambdaQuery()
            .eq(FinanceSupplier::getSupplierType, "SERVICE")
            .eq(FinanceSupplier::getStatus, "0")
            .list();
        return R.success(suppliers);
    }

    @Override
    public R<?> getMaterialSuppliers() {
        List<FinanceSupplier> suppliers = lambdaQuery()
            .eq(FinanceSupplier::getSupplierType, "MATERIAL")
            .eq(FinanceSupplier::getStatus, "0")
            .list();
        return R.success(suppliers);
    }

    @Override
    public R<?> evaluateSupplierPerformance(Long supplierId) {
        FinanceSupplier supplier = getById(supplierId);
        if (supplier == null) {
            return R.fail("供应商不存在");
        }
        
        // TODO: 实现供应商绩效评估逻辑
        return R.success("供应商评估完成");
    }
}