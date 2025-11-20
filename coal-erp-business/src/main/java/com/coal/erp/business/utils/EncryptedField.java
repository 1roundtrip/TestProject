package com.coal.erp.business.utils;

import java.lang.annotation.*;

/**
 * 加密字段注解
 * 标记需要加密存储的敏感字段
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptedField {
    String value() default "";
}