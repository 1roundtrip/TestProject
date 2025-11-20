package com.coal.erp.business.controller.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.asset.AssetStorage;
import com.coal.erp.business.domain.asset.AssetStorageDetail;
import com.coal.erp.business.service.asset.IAssetStorageService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 资产入库管理控制器
 */
@RestController
@RequestMapping("/api/asset/storage")
public class AssetStorageController {
    
    @Autowired
    private IAssetStorageService storageService;
    
    /**
     * 创建入库单
     */  
    @PostMapping
    @PreAuthorize("hasPermission(null, 'asset:storage:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            // 转换入库单数据
            @SuppressWarnings("unchecked")
            Map<String, Object> storageMap = (Map<String, Object>) params.get("storage");
            AssetStorage storage = convertToStorage(storageMap);
            
            // 转换明细数据
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<AssetStorageDetail> details = detailsMap.stream()
                .map(this::convertToDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return storageService.createStorage(storage, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 确认入库
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'asset:storage:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return storageService.confirmStorage(id);
    }
    
    /**
     * 取消入库
     */
    @PostMapping("/{id}/cancel")
    public R<?> cancel(@PathVariable Long id) {
        return storageService.cancelStorage(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'asset:storage:list')")
    public R<Page<AssetStorage>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String storageNo,
            @RequestParam(required = false) String status) {
        return R.success(storageService.pageStorage(current, size, storageNo, status));
    }
    
    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public R<AssetStorage> getById(@PathVariable Long id) {
        return R.success(storageService.getById(id));
    }
    
    /**
     * 获取入库明细
     */
    @GetMapping("/{id}/details")
    public R<List<AssetStorageDetail>> getDetails(@PathVariable Long id) {
        return R.success(storageService.getStorageDetails(id));
    }
    
    /**
     * 更新入库单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'asset:storage:edit')")
    public R<?> update(@RequestBody AssetStorage storage) {
        return R.success(storageService.updateById(storage));
    }
    
    /**
     * 删除入库单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'asset:storage:remove')")
    public R<?> delete(@PathVariable Long id) {
        AssetStorage storage = storageService.getById(id);
        if (storage != null && "CONFIRMED".equals(storage.getStatus())) {
            return R.error("已确认的入库单不能删除");
        }
        return R.success(storageService.removeById(id));
    }
    
    // 辅助方法：转换入库单数据
    private AssetStorage convertToStorage(Map<String, Object> map) {
        AssetStorage storage = new AssetStorage();
        if (map.get("storageType") != null) {
            storage.setStorageType(map.get("storageType").toString());
        }
        if (map.get("storageDate") != null) {
            storage.setStorageDate(java.sql.Date.valueOf(map.get("storageDate").toString()));
        }
        if (map.get("supplierId") != null) {
            storage.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        }
        if (map.get("supplierName") != null) {
            storage.setSupplierName(map.get("supplierName").toString());
        }
        if (map.get("warehouse") != null) {
            storage.setWarehouse(map.get("warehouse").toString());
        }
        if (map.get("location") != null) {
            storage.setLocation(map.get("location").toString());
        }
        if (map.get("remark") != null) {
            storage.setRemark(map.get("remark").toString());
        }
        return storage;
    }
    
    // 辅助方法：转换明细数据
    private AssetStorageDetail convertToDetail(Map<String, Object> map) {
        AssetStorageDetail detail = new AssetStorageDetail();
        if (map.get("assetId") != null) {
            detail.setAssetId(Long.valueOf(map.get("assetId").toString()));
        }
        if (map.get("assetCode") != null) {
            detail.setAssetCode(map.get("assetCode").toString());
        }
        if (map.get("assetName") != null) {
            detail.setAssetName(map.get("assetName").toString());
        }
        if (map.get("assetType") != null) {
            detail.setAssetType(map.get("assetType").toString());
        }
        if (map.get("category") != null) {
            detail.setCategory(map.get("category").toString());
        }
        if (map.get("manufacturer") != null) {
            detail.setManufacturer(map.get("manufacturer").toString());
        }
        if (map.get("model") != null) {
            detail.setModel(map.get("model").toString());
        }
        if (map.get("serialNumber") != null) {
            detail.setSerialNumber(map.get("serialNumber").toString());
        }
        if (map.get("quantity") != null) {
            detail.setQuantity(Integer.valueOf(map.get("quantity").toString()));
        }
        if (map.get("unitPrice") != null) {
            detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        }
        if (map.get("purchaseDate") != null) {
            detail.setPurchaseDate(java.sql.Date.valueOf(map.get("purchaseDate").toString()));
        }
        if (map.get("warrantyPeriod") != null) {
            detail.setWarrantyPeriod(Integer.valueOf(map.get("warrantyPeriod").toString()));
        }
        if (map.get("remark") != null) {
            detail.setRemark(map.get("remark").toString());
        }
        return detail;
    }
}

