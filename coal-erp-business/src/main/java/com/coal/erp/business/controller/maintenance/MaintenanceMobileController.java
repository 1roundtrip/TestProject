package com.coal.erp.business.controller.maintenance;

import com.coal.erp.business.service.maintenance.IMaintenanceMobileService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 移动维修控制器
 */
@RestController
@RequestMapping("/api/maintenance/mobile")
public class MaintenanceMobileController {
    
    @Autowired
    private IMaintenanceMobileService mobileService;
    
    @PostMapping("/checkin")
    @PreAuthorize("hasPermission(null, 'maintenance:mobile:checkin')")
    public R<?> checkin(@RequestBody Map<String, Object> params) {
        Long workOrderId = Long.valueOf(params.get("workOrderId").toString());
        Long technicianId = Long.valueOf(params.get("technicianId").toString());
        String checkinType = params.get("checkinType").toString();
        String location = params.get("location") != null ? params.get("location").toString() : null;
        java.math.BigDecimal latitude = params.get("latitude") != null ? 
            new java.math.BigDecimal(params.get("latitude").toString()) : null;
        java.math.BigDecimal longitude = params.get("longitude") != null ? 
            new java.math.BigDecimal(params.get("longitude").toString()) : null;
        return mobileService.checkin(workOrderId, technicianId, checkinType, location, latitude, longitude);
    }
    
    @GetMapping("/{workOrderId}/checkins")
    @PreAuthorize("hasPermission(null, 'maintenance:mobile:view')")
    public R<?> getCheckins(@PathVariable Long workOrderId) {
        return R.success(mobileService.getCheckinRecords(workOrderId));
    }
}

