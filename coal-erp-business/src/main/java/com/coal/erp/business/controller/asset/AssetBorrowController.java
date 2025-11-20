package com.coal.erp.business.controller.asset;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.asset.AssetBorrow;
import com.coal.erp.business.service.asset.IAssetBorrowService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 资产领用退库管理控制器
 */
@RestController
@RequestMapping("/api/asset/borrow")
public class AssetBorrowController {
    
    @Autowired
    private IAssetBorrowService borrowService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'asset:borrow:add')")
    public R<?> create(@RequestBody AssetBorrow borrow) {
        return borrowService.createBorrow(borrow);
    }
    
    @PostMapping("/{id}/return")
    @PreAuthorize("hasPermission(null, 'asset:borrow:return')")
    public R<?> returnAsset(@PathVariable Long id) {
        return borrowService.returnAsset(id);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'asset:borrow:list')")
    public R<Page<AssetBorrow>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String borrowNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String borrowType) {
        return R.success(borrowService.pageBorrow(current, size, borrowNo, status, borrowType));
    }
    
    @GetMapping("/{id}")
    public R<AssetBorrow> getById(@PathVariable Long id) {
        return R.success(borrowService.getById(id));
    }
    
    @PutMapping
    public R<?> update(@RequestBody AssetBorrow borrow) {
        return R.success(borrowService.updateById(borrow));
    }
    
    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable Long id) {
        return R.success(borrowService.removeById(id));
    }
    
    @GetMapping("/overdue")
    public R<?> getOverdueBorrows() {
        return borrowService.getOverdueBorrows();
    }
}

