package com.coal.erp.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制注解
 * 用于方法级别的权限验证
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermissions {
    
    /**
     * 需要的权限标识
     */
    String value();
    
    /**
     * 逻辑关系（AND/OR），默认为AND
     */
    Logical logical() default Logical.AND;
    
    /**
     * 权限逻辑枚举
     */
    enum Logical {
        AND, OR
    }
}