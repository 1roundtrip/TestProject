package com.coal.erp.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.system.domain.SysUser;
import com.coal.erp.system.domain.SysUserRole;
import com.coal.erp.system.mapper.SysUserRoleMapper;
import com.coal.erp.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/system/user")
public class SysUserController {
    
    @Autowired
    private ISysUserService userService;
    
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    
    @GetMapping("/page")
    // @PreAuthorize("hasPermission(null, 'system:user:list')")
    public R<Page<SysUser>> page(@RequestParam(defaultValue = "1") Long current,
                                  @RequestParam(defaultValue = "10") Long size,
                                  @RequestParam(required = false) String username) {
        Page<SysUser> page = new Page<>(current, size);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysUser::getUsername, username);
        }
        return R.success(userService.page(page, wrapper));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'system:user:add')")
    public R<?> add(@RequestBody SysUser user) {
        return R.success(userService.save(user));
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'system:user:edit')")
    public R<?> update(@RequestBody SysUser user) {
        try {
            if (user.getUserId() == null) {
                return R.error("用户ID不能为空");
            }
            // 如果密码为空，则不更新密码
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                // 从数据库获取原密码
                SysUser existingUser = userService.getById(user.getUserId());
                if (existingUser != null) {
                    user.setPassword(existingUser.getPassword());
                }
            }
            boolean result = userService.updateById(user);
            if (result) {
                return R.success();
            } else {
                return R.error("更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("更新用户失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'system:user:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(userService.removeById(id));
    }
    
    /**
     * 修改密码
     * 注意：内网环境暂时移除权限控制，允许所有已认证用户修改自己的密码
     */
    @PostMapping("/change-password")
    // @PreAuthorize("hasPermission(null, 'system:user:edit')")
    public R<?> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        return R.success();
    }
    
    /**
     * 获取用户角色列表
     */
    @GetMapping("/{userId}/roles")
    // @PreAuthorize("hasPermission(null, 'system:user:query')")
    public R<List<Long>> getUserRoles(@PathVariable Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = userRoleMapper.selectList(wrapper);
        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
        return R.success(roleIds);
    }
    
    /**
     * 分配用户角色
     */
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasPermission(null, 'system:user:edit')")
    public R<?> assignUserRoles(@PathVariable Long userId, @RequestBody AssignRolesRequest request) {
        // 删除原有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);
        
        // 添加新角色
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            for (Long roleId : request.getRoleIds()) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        
        return R.success();
    }
    
    /**
     * 修改密码请求对象
     */
    public static class ChangePasswordRequest {
        private Long userId;
        private String oldPassword;
        private String newPassword;
        
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        
        public String getOldPassword() {
            return oldPassword;
        }
        
        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }
        
        public String getNewPassword() {
            return newPassword;
        }
        
        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
    
    /**
     * 分配角色请求对象
     */
    public static class AssignRolesRequest {
        private List<Long> roleIds;
        
        public List<Long> getRoleIds() {
            return roleIds;
        }
        
        public void setRoleIds(List<Long> roleIds) {
            this.roleIds = roleIds;
        }
    }
}





