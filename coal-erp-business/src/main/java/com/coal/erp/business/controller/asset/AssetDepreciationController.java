package com.coal.erp.business.controller.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.asset.AssetDepreciation;
import com.coal.erp.business.domain.asset.AssetDepreciationDetail;
import com.coal.erp.business.service.asset.IAssetDepreciationService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产折旧管理控制器
 */
@RestController
@RequestMapping("/api/asset/depreciation")
public class AssetDepreciationController {
    
    @Autowired
    private IAssetDepreciationService depreciationService;
    
    @PostMapping("/config")
    @PreAuthorize("hasPermission(null, 'asset:depreciation:config')")
    public R<?> configDepreciation(@RequestBody AssetDepreciation depreciation) {
        return depreciationService.configDepreciation(depreciation);
    }
    
    @PostMapping("/calculate/{month}")
    @PreAuthorize("hasPermission(null, 'asset:depreciation:calculate')")
    public R<?> calculateDepreciation(@PathVariable String month) {
        return depreciationService.calculateDepreciation(month);
    }
    
    @PostMapping("/confirm/{detailId}")
    public R<?> confirmDepreciation(@PathVariable Long detailId) {
        return depreciationService.confirmDepreciation(detailId);
    }
    
    @GetMapping("/page")
    public R<Page<AssetDepreciation>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String assetCode,
            @RequestParam(required = false) String status) {
        return R.success(depreciationService.pageDepreciation(current, size, assetCode, status));
    }
    
    @GetMapping("/{id}")
    public R<AssetDepreciation> getById(@PathVariable Long id) {
        return R.success(depreciationService.getById(id));
    }
    
    @GetMapping("/{id}/details")
    public R<List<AssetDepreciationDetail>> getDetails(@PathVariable Long id) {
        return R.success(depreciationService.getDepreciationDetails(id));
    }
    
    @GetMapping("/month/{month}")
    public R<List<AssetDepreciationDetail>> getMonthDetails(@PathVariable String month) {
        return R.success(depreciationService.getMonthDepreciationDetails(month));
    }
    
    @PutMapping
    public R<?> update(@RequestBody AssetDepreciation depreciation) {
        return R.success(depreciationService.updateById(depreciation));
    }
    
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        return R.success(depreciationService.removeById(id));
    }
}

