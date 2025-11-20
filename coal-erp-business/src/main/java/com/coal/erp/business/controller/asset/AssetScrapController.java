package com.coal.erp.business.controller.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.asset.AssetScrap;
import com.coal.erp.business.service.asset.IAssetScrapService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 资产报废管理控制器
 */
@RestController
@RequestMapping("/api/asset/scrap")
public class AssetScrapController {
    
    @Autowired
    private IAssetScrapService scrapService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'asset:scrap:add')")
    public R<?> create(@RequestBody AssetScrap scrap) {
        return scrapService.createScrap(scrap);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'asset:scrap:approve')")
    public R<?> approve(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String approveRemark = params.get("approveRemark");
        return scrapService.approveScrap(id, approveRemark);
    }
    
    @PostMapping("/{id}/reject")
    public R<?> reject(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String rejectRemark = params.get("rejectRemark");
        return scrapService.rejectScrap(id, rejectRemark);
    }
    
    @PostMapping("/{id}/complete")
    public R<?> complete(@PathVariable Long id) {
        return scrapService.completeScrap(id);
    }
    
    @GetMapping("/page")
    public R<Page<AssetScrap>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String scrapNo,
            @RequestParam(required = false) String status) {
        return R.success(scrapService.pageScrap(current, size, scrapNo, status));
    }
    
    @GetMapping("/{id}")
    public R<AssetScrap> getById(@PathVariable Long id) {
        return R.success(scrapService.getById(id));
    }
    
    @PutMapping
    public R<?> update(@RequestBody AssetScrap scrap) {
        return R.success(scrapService.updateById(scrap));
    }
    
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        AssetScrap scrap = scrapService.getById(id);
        if (scrap != null && !"PENDING".equals(scrap.getStatus())) {
            return R.error("只能删除待审批状态的报废单");
        }
        return R.success(scrapService.removeById(id));
    }
}

