package com.coal.erp.common.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 */
public class PasswordUtils {
    
    /**
     * 生成BCrypt加密密码
     * 用于初始化数据库默认密码
     */
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String encodedPassword = encoder.encode(password);
        System.out.println("原始密码: " + password);
        System.out.println("加密后密码: " + encodedPassword);
        System.out.println();
        
        // 验证密码
        System.out.println("=== 密码验证测试 ===");
        String storedHash = "$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iwK8pJ6C";
        System.out.println("数据库中的密码哈希: " + storedHash);
        System.out.println("哈希格式检查: " + (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$") ? "正确" : "错误（缺少版本标识符）"));
        
        // 尝试修复格式（如果缺少版本标识符）
        if (storedHash.startsWith("$10$")) {
            String fixedHash = "$2a$10$" + storedHash.substring(4);
            System.out.println("修复后的哈希: " + fixedHash);
            boolean matches = encoder.matches(password, fixedHash);
            System.out.println("修复后密码匹配: " + matches);
        }
        
        // 生成新的正确哈希
        System.out.println();
        System.out.println("=== 生成新的正确哈希 ===");
        String newHash = encoder.encode(password);
        System.out.println("新生成的哈希: " + newHash);
        boolean newMatches = encoder.matches(password, newHash);
        System.out.println("新哈希密码匹配: " + newMatches);
        
        System.out.println();
        System.out.println("=== SQL更新语句 ===");
        System.out.println("UPDATE sys_user SET password = '" + newHash + "' WHERE username = 'admin';");
    }
}


