package com.coal.erp.system.security;

import com.coal.erp.system.domain.SysUser;
import com.coal.erp.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户认证服务
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private ISysUserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            SysUser user = userService.selectUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            
            // 获取用户权限（如果查询失败，返回空权限列表）
            List<String> perms = Collections.emptyList();
            try {
                perms = userService.selectPermsByUserId(user.getUserId());
            } catch (Exception e) {
                // 权限查询失败不影响登录，返回空权限列表
                // 日志已在 SysUserServiceImpl 中记录
            }
            
            List<GrantedAuthorityImpl> authorities = new ArrayList<>();
            if (perms != null) {
                for (String perm : perms) {
                    if (perm != null && !perm.trim().isEmpty()) {
                        authorities.add(new GrantedAuthorityImpl(perm));
                    }
                }
            }
            
            return new UserDetailsImpl(user, authorities);
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("加载用户信息失败: " + e.getMessage(), e);
        }
    }
}




