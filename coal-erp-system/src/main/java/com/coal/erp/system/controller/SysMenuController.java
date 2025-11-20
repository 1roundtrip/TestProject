package com.coal.erp.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.system.domain.SysMenu;
import com.coal.erp.system.mapper.SysMenuMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/api/system/menu")
public class SysMenuController {
    
    private static final Logger log = LoggerFactory.getLogger(SysMenuController.class);
    
    @Autowired
    private SysMenuMapper menuMapper;
    
    @GetMapping("/list")
    // @PreAuthorize("hasPermission(null, 'system:menu:list')")
    public R<List<SysMenu>> list() {
        try {
            LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
            // 先按 orderNum 排序，如果为 null 则按 menuId 排序
            wrapper.orderByAsc(SysMenu::getOrderNum)
                   .orderByAsc(SysMenu::getMenuId);
            List<SysMenu> menus = menuMapper.selectList(wrapper);
            return R.success(menus);
        } catch (Exception e) {
            log.error("查询菜单列表失败", e);
            // 如果排序失败，尝试不排序直接查询
            try {
                List<SysMenu> menus = menuMapper.selectList(null);
                return R.success(menus);
            } catch (Exception e2) {
                log.error("查询菜单列表失败（无排序）", e2);
                return R.error("查询菜单列表失败: " + e2.getMessage());
            }
        }
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'system:menu:add')")
    public R<?> add(@RequestBody SysMenu menu) {
        return R.success(menuMapper.insert(menu));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'system:menu:edit')")
    public R<?> update(@RequestBody SysMenu menu) {
        return R.success(menuMapper.updateById(menu));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'system:menu:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(menuMapper.deleteById(id));
    }
}





