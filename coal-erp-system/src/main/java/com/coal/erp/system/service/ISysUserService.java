package com.coal.erp.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.system.domain.SysUser;

import java.util.List;

/**
 * 用户服务接口
 */
public interface ISysUserService extends IService<SysUser> {
    
    /**
     * 根据用户名查询用户
     */
    SysUser selectUserByUsername(String username);
    
    /**
     * 根据用户ID查询权限列表
     */
    List<String> selectPermsByUserId(Long userId);
    
    /**
     * 登录
     */
    String login(String username, String password);
    
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}





