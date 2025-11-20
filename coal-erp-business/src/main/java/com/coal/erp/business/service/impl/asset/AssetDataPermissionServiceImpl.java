package com.coal.erp.business.service.impl.asset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.service.asset.IAssetDataPermissionService;
import com.coal.erp.common.utils.SecurityUtils;
import com.coal.erp.system.domain.SysUser;
import com.coal.erp.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产数据权限服务实现
 */
@Service
public class AssetDataPermissionServiceImpl implements IAssetDataPermissionService {
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Override
    public void addDataPermissionFilter(LambdaQueryWrapper<Asset> wrapper, Long userId) {
        if (userId == null) {
            userId = SecurityUtils.getUserId();
        }
        
        // 获取用户信息
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            // 用户不存在，不允许查询 - 使用一个不可能存在的ID
            wrapper.eq(Asset::getAssetId, -1L);
            return;
        }
        
        // 获取用户有权限的部门ID列表
        List<Long> accessibleDeptIds = getAccessibleDeptIds(userId);
        
        if (accessibleDeptIds.isEmpty()) {
            // 没有权限访问任何部门，不允许查询
            wrapper.eq(Asset::getAssetId, -1L);
        } else if (accessibleDeptIds.size() == 1) {
            // 只有一个部门，直接过滤
            wrapper.eq(Asset::getDeptId, accessibleDeptIds.get(0));
        } else {
            // 多个部门，使用IN查询
            wrapper.in(Asset::getDeptId, accessibleDeptIds);
        }
    }
    
    @Override
    public boolean hasDeptPermission(Long userId, Long deptId) {
        if (userId == null || deptId == null) {
            return false;
        }
        
        List<Long> accessibleDeptIds = getAccessibleDeptIds(userId);
        return accessibleDeptIds.contains(deptId);
    }
    
    @Override
    public List<Long> getAccessibleDeptIds(Long userId) {
        List<Long> deptIds = new ArrayList<>();
        
        // 获取用户信息
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            return deptIds;
        }
        
        // 如果用户有部门，添加该部门
        if (user.getDeptId() != null) {
            deptIds.add(user.getDeptId());
        }
        
        // TODO: 可以根据角色权限扩展，例如：
        // - 管理员角色可以访问所有部门
        // - 部门经理可以访问本部门及子部门
        // - 普通员工只能访问本部门
        
        // 如果用户是管理员（可以根据角色判断），返回空列表表示可以访问所有部门
        // 这里简化处理，实际应该根据角色权限判断
        
        return deptIds;
    }
}

