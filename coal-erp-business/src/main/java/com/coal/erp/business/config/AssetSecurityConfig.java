package com.coal.erp.business.config;

import com.coal.erp.common.annotation.RequiresPermissions;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

/**
 * 资产管理模块权限配置
 */
@Configuration
public class AssetSecurityConfig {

    /**
     * 资产管理基础权限注解
     */
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @PreAuthorize("hasPermission(null, 'asset')")
    public @interface RequiresAssetPermission {
        String value() default "";
    }

    /**
     * 资产档案管理权限
     */
    @RequiresPermissions("asset:archive")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresArchivePermission {
        String value() default "";
    }

    /**
     * 资产入库管理权限
     */
    @RequiresPermissions("asset:storage")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresStoragePermission {
        String value() default "";
    }

    /**
     * 资产领用退库权限
     */
    @RequiresPermissions("asset:borrow")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresBorrowPermission {
        String value() default "";
    }

    /**
     * 资产转移调拨权限
     */
    @RequiresPermissions("asset:transfer")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresTransferPermission {
        String value() default "";
    }

    /**
     * 资产折旧管理权限
     */
    @RequiresPermissions("asset:depreciation")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresDepreciationPermission {
        String value() default "";
    }

    /**
     * 资产盘点管理权限
     */
    @RequiresPermissions("asset:inventory")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresInventoryPermission {
        String value() default "";
    }

    /**
     * 资产报废管理权限
     */
    @RequiresPermissions("asset:scrap")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresScrapPermission {
        String value() default "";
    }

    /**
     * 资产报表分析权限
     */
    @RequiresPermissions("asset:report")
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresReportPermission {
        String value() default "";
    }
}

