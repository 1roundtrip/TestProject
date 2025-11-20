package com.coal.erp.system.security;

import com.coal.erp.system.domain.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 用户详情实现
 */
public class UserDetailsImpl implements UserDetails {
    
    private SysUser user;
    private Collection<? extends GrantedAuthority> authorities;
    
    public UserDetailsImpl(SysUser user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return "0".equals(user.getStatus());
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return "0".equals(user.getStatus());
    }
    
    public Long getUserId() {
        return user.getUserId();
    }
}















