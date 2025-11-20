package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenanceTeam;
import com.coal.erp.business.domain.maintenance.MaintenanceTeamMember;
import com.coal.erp.business.mapper.maintenance.MaintenanceTeamMapper;
import com.coal.erp.business.mapper.maintenance.MaintenanceTeamMemberMapper;
import com.coal.erp.business.service.maintenance.IMaintenanceTeamService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 维修团队服务实现
 */
@Service
public class MaintenanceTeamServiceImpl extends ServiceImpl<MaintenanceTeamMapper, MaintenanceTeam> 
        implements IMaintenanceTeamService {
    
    @Autowired
    private MaintenanceTeamMemberMapper memberMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createTeam(MaintenanceTeam team, List<Long> memberIds) {
        try {
            if (team.getTeamCode() == null || team.getTeamCode().isEmpty()) {
                team.setTeamCode("MT" + System.currentTimeMillis());
            }
            team.setStatus("ACTIVE");
            team.setCreateTime(new Date());
            team.setUpdateTime(new Date());
            save(team);
            
            if (memberIds != null && !memberIds.isEmpty()) {
                for (Long userId : memberIds) {
                    MaintenanceTeamMember member = new MaintenanceTeamMember();
                    member.setTeamId(team.getTeamId());
                    member.setUserId(userId);
                    member.setRole("MEMBER");
                    member.setStatus("ACTIVE");
                    member.setCreateTime(new Date());
                    memberMapper.insert(member);
                }
                team.setMemberCount(memberIds.size());
                updateById(team);
            }
            
            return R.success(team);
        } catch (Exception e) {
            return R.error("创建团队失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenanceTeam> pageTeam(Long current, Long size, String teamName, String status) {
        Page<MaintenanceTeam> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenanceTeam> wrapper = new LambdaQueryWrapper<>();
        if (teamName != null && !teamName.isEmpty()) {
            wrapper.like(MaintenanceTeam::getTeamName, teamName);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MaintenanceTeam::getStatus, status);
        }
        wrapper.orderByDesc(MaintenanceTeam::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<MaintenanceTeamMember> getTeamMembers(Long teamId) {
        LambdaQueryWrapper<MaintenanceTeamMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaintenanceTeamMember::getTeamId, teamId);
        return memberMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> addMember(Long teamId, Long userId) {
        try {
            LambdaQueryWrapper<MaintenanceTeamMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MaintenanceTeamMember::getTeamId, teamId);
            wrapper.eq(MaintenanceTeamMember::getUserId, userId);
            if (memberMapper.selectCount(wrapper) > 0) {
                return R.error("成员已存在");
            }
            
            MaintenanceTeamMember member = new MaintenanceTeamMember();
            member.setTeamId(teamId);
            member.setUserId(userId);
            member.setRole("MEMBER");
            member.setStatus("ACTIVE");
            member.setCreateTime(new Date());
            memberMapper.insert(member);
            
            MaintenanceTeam team = getById(teamId);
            if (team != null) {
                team.setMemberCount(team.getMemberCount() + 1);
                updateById(team);
            }
            
            return R.success();
        } catch (Exception e) {
            return R.error("添加成员失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> removeMember(Long teamId, Long userId) {
        try {
            LambdaQueryWrapper<MaintenanceTeamMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MaintenanceTeamMember::getTeamId, teamId);
            wrapper.eq(MaintenanceTeamMember::getUserId, userId);
            memberMapper.delete(wrapper);
            
            MaintenanceTeam team = getById(teamId);
            if (team != null && team.getMemberCount() > 0) {
                team.setMemberCount(team.getMemberCount() - 1);
                updateById(team);
            }
            
            return R.success();
        } catch (Exception e) {
            return R.error("移除成员失败：" + e.getMessage());
        }
    }
}

