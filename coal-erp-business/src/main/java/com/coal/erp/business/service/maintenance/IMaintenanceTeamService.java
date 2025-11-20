package com.coal.erp.business.service.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.maintenance.MaintenanceTeam;
import com.coal.erp.business.domain.maintenance.MaintenanceTeamMember;
import com.coal.erp.common.core.domain.R;

import java.util.List;

/**
 * 维修团队服务接口
 */
public interface IMaintenanceTeamService extends IService<MaintenanceTeam> {
    
    /**
     * 创建团队
     */
    R<?> createTeam(MaintenanceTeam team, List<Long> memberIds);
    
    /**
     * 分页查询团队
     */
    Page<MaintenanceTeam> pageTeam(Long current, Long size, String teamName, String status);
    
    /**
     * 获取团队成员
     */
    List<MaintenanceTeamMember> getTeamMembers(Long teamId);
    
    /**
     * 添加成员
     */
    R<?> addMember(Long teamId, Long userId);
    
    /**
     * 移除成员
     */
    R<?> removeMember(Long teamId, Long userId);
}

