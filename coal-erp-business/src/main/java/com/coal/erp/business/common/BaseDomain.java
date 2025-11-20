package com.coal.erp.business.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 * 包含所有实体必须的审计字段
 */
@Data
public abstract class BaseDomain implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 创建人ID
     */
    private Long createUserId;
    
    /**
     * 创建人姓名
     */
    private String createUserName;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 备注
     */
    private String remark;
}

