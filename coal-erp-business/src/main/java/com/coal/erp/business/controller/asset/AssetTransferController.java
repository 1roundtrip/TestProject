package com.coal.erp.business.controller.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.asset.AssetTransfer;
import com.coal.erp.business.service.asset.IAssetTransferService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 资产转移调拨管理控制器
 */
@RestController
@RequestMapping("/api/asset/transfer")
public class AssetTransferController {
    
    @Autowired
    private IAssetTransferService transferService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'asset:transfer:add')")
    public R<?> create(@RequestBody AssetTransfer transfer) {
        return transferService.createTransfer(transfer);
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'asset:transfer:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return transferService.approveTransfer(id, approveRemark);
    }
    
    @PostMapping("/{id}/reject")
    public R<?> reject(@PathVariable Long id, @RequestParam(required = false) String rejectRemark) {
        return transferService.rejectTransfer(id, rejectRemark);
    }
    
    @PostMapping("/{id}/execute")
    public R<?> execute(@PathVariable Long id) {
        return transferService.executeTransfer(id);
    }
    
    @GetMapping("/page")
    public R<Page<AssetTransfer>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String transferNo,
            @RequestParam(required = false) String status) {
        return R.success(transferService.pageTransfer(current, size, transferNo, status));
    }
    
    @GetMapping("/{id}")
    public R<AssetTransfer> getById(@PathVariable Long id) {
        return R.success(transferService.getById(id));
    }
    
    @PutMapping
    public R<?> update(@RequestBody AssetTransfer transfer) {
        return R.success(transferService.updateById(transfer));
    }
    
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        return R.success(transferService.removeById(id));
    }
}

