package com.coal.erp.business.config;

import com.coal.erp.common.annotation.RequiresPermissions;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

/**
 * 财务模块权限配置
 */
@Configuration
public class FinanceSecurityConfig {

    /**
     * 财务模块权限注解
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @PreAuthorize("@ss.hasPermi('finance')")
    public @interface RequiresFinancePermission {
        String value() default "";
    }

    /**
     * 凭证管理权限
     */
    @RequiresPermissions("finance:voucher")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresVoucherPermission {
        String value() default "";
    }

    /**
     * 科目管理权限
     */
    @RequiresPermissions("finance:subject")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresSubjectPermission {
        String value() default "";
    }

    /**
     * 账簿查询权限
     */
    @RequiresPermissions("finance:book")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresBookPermission {
        String value() default "";
    }

    /**
     * 期末处理权限
     */
    @RequiresPermissions("finance:period")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresPeriodPermission {
        String value() default "";
    }

    /**
     * 报表查询权限
     */
    @RequiresPermissions("finance:report")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresReportPermission {
        String value() default "";
    }
}
