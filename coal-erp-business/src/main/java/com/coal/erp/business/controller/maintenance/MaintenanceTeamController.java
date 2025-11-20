package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenanceTeam;
import com.coal.erp.business.service.maintenance.IMaintenanceTeamService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 维修团队控制器
 */
@RestController
@RequestMapping("/api/maintenance/team")
public class MaintenanceTeamController {
    
    @Autowired
    private IMaintenanceTeamService teamService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:team:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        Map<String, Object> teamMap = (Map<String, Object>) params.get("team");
        MaintenanceTeam team = convertToTeam(teamMap);
        @SuppressWarnings("unchecked")
        List<Long> memberIds = (List<Long>) params.get("memberIds");
        return teamService.createTeam(team, memberIds);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:team:list')")
    public R<Page<MaintenanceTeam>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String teamName,
            @RequestParam(required = false) String status) {
        return R.success(teamService.pageTeam(current, size, teamName, status));
    }
    
    @GetMapping("/{id}")
    public R<MaintenanceTeam> getById(@PathVariable Long id) {
        return R.success(teamService.getById(id));
    }
    
    @GetMapping("/{id}/members")
    public R<?> getMembers(@PathVariable Long id) {
        return R.success(teamService.getTeamMembers(id));
    }
    
    @PostMapping("/{id}/members")
    @PreAuthorize("hasPermission(null, 'maintenance:team:edit')")
    public R<?> addMember(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        return teamService.addMember(id, userId);
    }
    
    @DeleteMapping("/{id}/members/{userId}")
    @PreAuthorize("hasPermission(null, 'maintenance:team:edit')")
    public R<?> removeMember(@PathVariable Long id, @PathVariable Long userId) {
        return teamService.removeMember(id, userId);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:team:edit')")
    public R<?> update(@RequestBody MaintenanceTeam team) {
        return R.success(teamService.updateById(team));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:team:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(teamService.removeById(id));
    }
    
    private MaintenanceTeam convertToTeam(Map<String, Object> map) {
        MaintenanceTeam team = new MaintenanceTeam();
        if (map.get("teamCode") != null) team.setTeamCode(map.get("teamCode").toString());
        if (map.get("teamName") != null) team.setTeamName(map.get("teamName").toString());
        if (map.get("teamType") != null) team.setTeamType(map.get("teamType").toString());
        if (map.get("leaderId") != null) team.setLeaderId(Long.valueOf(map.get("leaderId").toString()));
        if (map.get("specialty") != null) team.setSpecialty(map.get("specialty").toString());
        return team;
    }
}

