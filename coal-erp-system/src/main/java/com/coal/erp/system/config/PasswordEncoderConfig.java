package com.coal.erp.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器配置
 * 内网环境简化版本：使用明文密码，不做加密处理
 * 注意：仅适用于内网环境，生产环境建议使用BCrypt加密
 */
@Configuration
public class PasswordEncoderConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用自定义的明文密码编码器，不做任何加密处理，直接比较明文
        return new PlainPasswordEncoder();
    }
}


