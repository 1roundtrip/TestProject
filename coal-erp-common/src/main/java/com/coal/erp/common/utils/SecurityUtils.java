package com.coal.erp.common.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityUtils {

    @Value("${hr.encrypt.key:coal_erp_default_key_2024}")
    private String encryptKey;
    
    private AES aes;

    @PostConstruct
    public void init() {
        // 使用Hutool的AES加密，确保密钥长度为16/24/32位
        String key = ensureKeyLength(encryptKey);
        aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 加密字符串
     */
    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        return aes.encryptHex(data);
    }

    /**
     * 解密字符串
     */
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        try {
            return aes.decryptStr(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 确保密钥长度符合AES要求
     */
    private String ensureKeyLength(String key) {
        if (key == null) {
            key = "coal_erp_default_key_2024";
        }
        
        // AES密钥长度要求：16/24/32字节
        int requiredLength = 32; // 使用256位加密
        if (key.length() < requiredLength) {
            // 密钥不足时填充
            StringBuilder builder = new StringBuilder(key);
            while (builder.length() < requiredLength) {
                builder.append("0");
            }
            return builder.toString().substring(0, requiredLength);
        }
        return key;
    }

    /**
     * 掩码显示敏感信息
     */
    public static String maskSensitiveInfo(String info) {
        if (info == null || info.isEmpty()) {
            return "****";
        }
        
        int length = info.length();
        int visible = Math.min(3, length / 3); // 显示前1/3
        int maskLength = length - visible * 2;
        
        if (maskLength <= 0) {
            return "****";
        }
        
        String start = info.substring(0, visible);
        String end = info.substring(length - visible);
        
        StringBuilder mask = new StringBuilder();
        for (int i = 0; i < maskLength; i++) {
            mask.append("*");
        }
        return start + mask.toString() + end;
    }
    
    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    // 从UserDetails中获取用户ID（需要根据实际实现调整）
                    return 1L; // 临时返回，实际应该从principal中获取
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return 1L; // 默认返回1
    }
    
    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    return ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
                }
                return principal.toString();
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return "system"; // 默认返回system
    }
}