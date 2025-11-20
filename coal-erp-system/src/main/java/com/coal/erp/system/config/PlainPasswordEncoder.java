package com.coal.erp.system.config;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 明文密码编码器（内网环境简化版本）
 * 不做任何加密处理，直接返回原始密码
 * 注意：仅适用于内网环境，生产环境建议使用BCrypt加密
 */
public class PlainPasswordEncoder implements PasswordEncoder {
    
    @Override
    public String encode(CharSequence rawPassword) {
        // 直接返回原始密码，不做加密
        return rawPassword.toString();
    }
    
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 直接比较明文密码
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return rawPassword.toString().equals(encodedPassword);
    }
}

