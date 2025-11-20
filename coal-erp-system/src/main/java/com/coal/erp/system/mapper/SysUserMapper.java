package com.coal.erp.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.system.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 使用原生SQL查询用户（备用方法，用于调试）
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser selectByUsernameNative(@Param("username") String username);
    
    /**
     * 使用不区分大小写的SQL查询用户（备用方法）
     */
    @Select("SELECT * FROM sys_user WHERE LOWER(TRIM(username)) = LOWER(TRIM(#{username}))")
    SysUser selectByUsernameCaseInsensitive(@Param("username") String username);
}











