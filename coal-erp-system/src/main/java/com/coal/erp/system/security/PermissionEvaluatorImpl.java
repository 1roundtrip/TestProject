package com.coal.erp.system.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.stream.Collectors;

/**
 * 权限评估器（用于按钮级权限控制）
 */
@Slf4j
@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {
    
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || permission == null) {
            log.warn("权限检查失败: authentication={}, permission={}", authentication, permission);
            return false;
        }
        
        String perm = permission.toString();
        
        // 获取所有权限列表用于日志
        String authoritiesStr = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        
        log.debug("权限检查: 需要权限={}, 用户权限列表=[{}]", perm, authoritiesStr);
        
        // 检查用户是否有该权限
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(perm)) {
                log.debug("权限检查通过: {}", perm);
                return true;
            }
        }
        
        log.warn("权限检查失败: 用户缺少权限 {}, 当前权限列表=[{}]", perm, authoritiesStr);
        return false;
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}












