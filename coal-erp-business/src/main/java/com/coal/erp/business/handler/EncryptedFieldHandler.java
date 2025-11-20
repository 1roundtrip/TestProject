package com.coal.erp.business.handler;

import com.coal.erp.common.utils.SecurityUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 加密字段类型处理器
 */
@Component
public class EncryptedFieldHandler extends BaseTypeHandler<String> {

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter != null && !parameter.isEmpty()) {
            String encrypted = securityUtils.encrypt(parameter);
            ps.setString(i, encrypted);
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String encrypted = rs.getString(columnName);
        // 如果字段值为 null 或空，直接返回
        if (encrypted == null || encrypted.isEmpty()) {
            return encrypted;
        }
        // 排除不应该加密的字段（如 parent_id 等数字字段）
        // 如果值看起来像数字（不是加密字符串），直接返回原值
        if (isNumeric(encrypted)) {
            return encrypted;
        }
        // 尝试解密，如果失败则返回原值
        try {
            return securityUtils.decrypt(encrypted);
        } catch (Exception e) {
            // 解密失败，说明不是加密数据，返回原值
            return encrypted;
        }
    }
    
    /**
     * 判断字符串是否为数字
     */
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String encrypted = rs.getString(columnIndex);
        // 如果字段值为 null 或空，直接返回
        if (encrypted == null || encrypted.isEmpty()) {
            return encrypted;
        }
        // 排除不应该加密的字段（如 parent_id 等数字字段）
        // 如果值看起来像数字（不是加密字符串），直接返回原值
        if (isNumeric(encrypted)) {
            return encrypted;
        }
        // 尝试解密，如果失败则返回原值
        try {
            return securityUtils.decrypt(encrypted);
        } catch (Exception e) {
            // 解密失败，说明不是加密数据，返回原值
            return encrypted;
        }
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String encrypted = cs.getString(columnIndex);
        // 如果字段值为 null 或空，直接返回
        if (encrypted == null || encrypted.isEmpty()) {
            return encrypted;
        }
        // 排除不应该加密的字段（如 parent_id 等数字字段）
        // 如果值看起来像数字（不是加密字符串），直接返回原值
        if (isNumeric(encrypted)) {
            return encrypted;
        }
        // 尝试解密，如果失败则返回原值
        try {
            return securityUtils.decrypt(encrypted);
        } catch (Exception e) {
            // 解密失败，说明不是加密数据，返回原值
            return encrypted;
        }
    }
}