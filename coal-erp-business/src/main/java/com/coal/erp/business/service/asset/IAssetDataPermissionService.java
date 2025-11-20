package com.coal.erp.business.service.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.Asset;

/**
 * 资产数据权限服务接口
 * 用于实现按部门过滤资产数据
 */
public interface IAssetDataPermissionService {
    
    /**
     * 为资产查询添加数据权限过滤
     * @param wrapper 查询条件包装器
     * @param userId 用户ID
     */
    void addDataPermissionFilter(LambdaQueryWrapper<Asset> wrapper, Long userId);
    
    /**
     * 检查用户是否有权限访问指定部门的资产
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return true-有权限，false-无权限
     */
    boolean hasDeptPermission(Long userId, Long deptId);
    
    /**
     * 获取用户有权限访问的部门ID列表
     * @param userId 用户ID
     * @return 部门ID列表
     */
    java.util.List<Long> getAccessibleDeptIds(Long userId);
}

