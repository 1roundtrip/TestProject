package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修团队表
 */
@Data
@TableName("maintenance_team")
public class MaintenanceTeam implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long teamId;
    
    private String teamCode;
    
    private String teamName;
    
    private String teamType;
    
    private Long leaderId;
    
    private String leaderName;
    
    private Integer memberCount;
    
    private String specialty;
    
    private String status;
    
    private Long createUserId;
    
    private String createUserName;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

