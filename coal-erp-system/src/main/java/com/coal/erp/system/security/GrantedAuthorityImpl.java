package com.coal.erp.system.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * 权限实现
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    
    private String authority;
    
    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }
    
    @Override
    public String getAuthority() {
        return authority;
    }
}















