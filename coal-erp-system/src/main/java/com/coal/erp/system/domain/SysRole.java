package com.coal.erp.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 */
@Data
@TableName("sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long roleId;
    
    private String roleName;
    
    private String roleKey;
    
    private Integer roleSort;
    
    private String status;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















