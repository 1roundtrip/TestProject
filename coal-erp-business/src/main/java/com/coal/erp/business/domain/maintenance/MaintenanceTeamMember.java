package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修团队成员表
 */
@Data
@TableName("maintenance_team_member")
public class MaintenanceTeamMember implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long memberId;
    
    private Long teamId;
    
    private Long userId;
    
    private String userName;
    
    private String role;
    
    private String skillLevel;
    
    private String specialty;
    
    private Date joinDate;
    
    private String status;
    
    private Date createTime;
    
    private Date updateTime;
}

