package com.coal.erp.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.service.IAssetService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产中心控制器
 */
@RestController
@RequestMapping("/api/asset")
public class AssetController {
    
    @Autowired
    private IAssetService assetService;
    
    /**
     * 分页查询资产
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'asset:archive:list')")
    public R<Page<Asset>> page(@RequestParam(defaultValue = "1") Long current,
                               @RequestParam(defaultValue = "10") Long size,
                               @RequestParam(required = false) String assetName) {
        Page<Asset> page = new Page<>(current, size);
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        if (assetName != null && !assetName.isEmpty()) {
            wrapper.like(Asset::getAssetName, assetName);
        }
        // TODO: 添加数据权限过滤（按部门）
        return R.success(assetService.page(page, wrapper));
    }
    
    /**
     * 新增资产
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'asset:archive:add')")
    public R<?> add(@RequestBody Asset asset) {
        return R.success(assetService.save(asset));
    }
    
    /**
     * 修改资产
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'asset:archive:edit')")
    public R<?> update(@RequestBody Asset asset) {
        return R.success(assetService.updateById(asset));
    }
    
    /**
     * 删除资产
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'asset:archive:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(assetService.removeById(id));
    }
    
    /**
     * 根据ID查询资产
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户访问
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasPermission(null, 'asset:query')")
    public R<Asset> getById(@PathVariable Long id) {
        return R.success(assetService.getById(id));
    }
    
    /**
     * 获取所有资产类型（去重）
     */
    @GetMapping("/types")
    public R<List<String>> getAssetTypes() {
        List<Asset> assets = assetService.list();
        List<String> types = assets.stream()
            .map(Asset::getAssetType)
            .filter(type -> type != null && !type.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        return R.success(types);
    }
    
    /**
     * 获取所有制造商（去重）
     */
    @GetMapping("/manufacturers")
    public R<List<String>> getManufacturers() {
        List<Asset> assets = assetService.list();
        List<String> manufacturers = assets.stream()
            .map(Asset::getManufacturer)
            .filter(manufacturer -> manufacturer != null && !manufacturer.isEmpty())
            .distinct()
            .sorted()
            .collect(java.util.stream.Collectors.toList());
        return R.success(manufacturers);
    }
}





