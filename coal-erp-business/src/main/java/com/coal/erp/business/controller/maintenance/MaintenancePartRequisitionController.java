package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenancePartRequisition;
import com.coal.erp.business.domain.maintenance.MaintenancePartRequisitionDetail;
import com.coal.erp.business.service.maintenance.IMaintenancePartRequisitionService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 维修备件领用控制器
 */
@RestController
@RequestMapping("/api/maintenance/part")
public class MaintenancePartRequisitionController {
    
    @Autowired
    private IMaintenancePartRequisitionService requisitionService;
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:part:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        Map<String, Object> requisitionMap = (Map<String, Object>) params.get("requisition");
        MaintenancePartRequisition requisition = convertToRequisition(requisitionMap);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
        List<MaintenancePartRequisitionDetail> details = null;
        if (detailsMap != null) {
            details = detailsMap.stream().map(this::convertToDetail).collect(java.util.stream.Collectors.toList());
        }
        return requisitionService.createRequisition(requisition, details);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:part:list')")
    public R<Page<MaintenancePartRequisition>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String requisitionNo,
            @RequestParam(required = false) String status) {
        return R.success(requisitionService.pageRequisition(current, size, requisitionNo, status));
    }
    
    @GetMapping("/{id}")
    public R<MaintenancePartRequisition> getById(@PathVariable Long id) {
        return R.success(requisitionService.getById(id));
    }
    
    @GetMapping("/{id}/details")
    public R<List<MaintenancePartRequisitionDetail>> getDetails(@PathVariable Long id) {
        return R.success(requisitionService.getRequisitionDetails(id));
    }
    
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'maintenance:part:approve')")
    public R<?> approve(@PathVariable Long id) {
        return requisitionService.approveRequisition(id);
    }
    
    @PostMapping("/{id}/issue")
    @PreAuthorize("hasPermission(null, 'maintenance:part:issue')")
    public R<?> issue(@PathVariable Long id) {
        return requisitionService.issueRequisition(id);
    }
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:part:edit')")
    public R<?> update(@RequestBody MaintenancePartRequisition requisition) {
        return R.success(requisitionService.updateById(requisition));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:part:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(requisitionService.removeById(id));
    }
    
    private MaintenancePartRequisition convertToRequisition(Map<String, Object> map) {
        MaintenancePartRequisition requisition = new MaintenancePartRequisition();
        if (map.get("workOrderId") != null) requisition.setWorkOrderId(Long.valueOf(map.get("workOrderId").toString()));
        if (map.get("requisitionType") != null) requisition.setRequisitionType(map.get("requisitionType").toString());
        return requisition;
    }
    
    private MaintenancePartRequisitionDetail convertToDetail(Map<String, Object> map) {
        MaintenancePartRequisitionDetail detail = new MaintenancePartRequisitionDetail();
        if (map.get("materialId") != null) detail.setMaterialId(Long.valueOf(map.get("materialId").toString()));
        if (map.get("materialName") != null) detail.setMaterialName(map.get("materialName").toString());
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        if (map.get("unitPrice") != null) detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        return detail;
    }
}

