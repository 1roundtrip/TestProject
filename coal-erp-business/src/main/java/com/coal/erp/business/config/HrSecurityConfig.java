package com.coal.erp.business.config;

import com.coal.erp.common.annotation.RequiresPermissions;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

/**
 * HR模块权限配置
 */
@Configuration
public class HrSecurityConfig {

    /**
     * HR基础权限注解
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @PreAuthorize("@ss.hasPermi('hr')")
    public @interface RequiresHrPermission {
        String value() default "";
    }

    /**
     * 部门管理权限
     */
    @RequiresPermissions("hr:department")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresDepartmentPermission {
        String value() default "";
    }

    /**
     * 员工管理权限
     */
    @RequiresPermissions("hr:employee")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresEmployeePermission {
        String value() default "";
    }

    /**
     * 合同管理权限
     */
    @RequiresPermissions("hr:contract")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresContractPermission {
        String value() default "";
    }

    /**
     * 证照管理权限
     */
    @RequiresPermissions("hr:certificate")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresCertificatePermission {
        String value() default "";
    }

    /**
     * 安全培训权限
     */
    @RequiresPermissions("hr:training")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresTrainingPermission {
        String value() default "";
    }

    /**
     * 健康档案权限
     */
    @RequiresPermissions("hr:health")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresHealthPermission {
        String value() default "";
    }
}