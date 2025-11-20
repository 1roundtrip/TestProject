package com.coal.erp.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.system.domain.SysRole;
import com.coal.erp.system.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/system/role")
public class SysRoleController {
    
    @Autowired
    private SysRoleMapper roleMapper;
    
    @GetMapping("/page")
    // @PreAuthorize("hasPermission(null, 'system:role:list')")
    public R<Page<SysRole>> page(@RequestParam(defaultValue = "1") Long current,
                                  @RequestParam(defaultValue = "10") Long size) {
        Page<SysRole> page = new Page<>(current, size);
        return R.success(roleMapper.selectPage(page, null));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'system:role:add')")
    public R<?> add(@RequestBody SysRole role) {
        return R.success(roleMapper.insert(role));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'system:role:edit')")
    public R<?> update(@RequestBody SysRole role) {
        return R.success(roleMapper.updateById(role));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'system:role:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(roleMapper.deleteById(id));
    }
}





