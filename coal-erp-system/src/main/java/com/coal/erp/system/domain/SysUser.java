package com.coal.erp.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long userId;
    
    private String username;
    
    private String password;
    
    private String nickName;
    
    private String email;
    
    private String phone;
    
    private String sex;
    
    private String avatar;
    
    private String status;
    
    private Long deptId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















