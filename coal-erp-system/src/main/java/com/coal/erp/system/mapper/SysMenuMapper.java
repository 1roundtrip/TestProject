package com.coal.erp.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.system.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单Mapper
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    
    /**
     * 根据用户ID查询权限
     */
    List<String> selectPermsByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询权限
     */
    List<String> selectPermsByRoleId(@Param("roleId") Long roleId);
}















