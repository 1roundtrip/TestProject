package com.coal.erp.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门表
 */
@Data
@TableName("sys_dept")
public class SysDept implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long deptId;
    
    private String deptName;
    
    private Long parentId;
    
    private Integer orderNum;
    
    private String leader;
    
    private String phone;
    
    private String email;
    
    private String status;
    
    private Date createTime;
    
    private Date updateTime;
}















