package com.coal.erp.business.controller.maintenance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrder;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrderDetail;
import com.coal.erp.business.service.maintenance.IMaintenanceWorkOrderService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 维修工单管理控制器
 */
@RestController
@RequestMapping("/api/maintenance/work-order")
public class MaintenanceWorkOrderController {
    
    @Autowired
    private IMaintenanceWorkOrderService workOrderService;
    
    /**
     * 创建维修工单
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> workOrderMap = (Map<String, Object>) params.get("workOrder");
            MaintenanceWorkOrder workOrder = convertToWorkOrder(workOrderMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<MaintenanceWorkOrderDetail> details = null;
            if (detailsMap != null) {
                details = detailsMap.stream()
                    .map(this::convertToDetail)
                    .collect(java.util.stream.Collectors.toList());
            }
            
            return workOrderService.createWorkOrder(workOrder, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:list')")
    public R<Page<MaintenanceWorkOrder>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String workOrderNo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long assetId) {
        return R.success(workOrderService.pageWorkOrder(current, size, workOrderNo, status, assetId));
    }
    
    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public R<MaintenanceWorkOrder> getById(@PathVariable Long id) {
        return R.success(workOrderService.getById(id));
    }
    
    /**
     * 获取工单明细
     */
    @GetMapping("/{id}/details")
    public R<List<MaintenanceWorkOrderDetail>> getDetails(@PathVariable Long id) {
        return R.success(workOrderService.getWorkOrderDetails(id));
    }
    
    /**
     * 分配工单
     */
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:assign')")
    public R<?> assign(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        Long teamId = params.get("teamId") != null ? Long.valueOf(params.get("teamId").toString()) : null;
        Long technicianId = params.get("technicianId") != null ? Long.valueOf(params.get("technicianId").toString()) : null;
        return workOrderService.assignWorkOrder(id, teamId, technicianId);
    }
    
    /**
     * 开始维修
     */
    @PostMapping("/{id}/start")
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:start')")
    public R<?> start(@PathVariable Long id) {
        return workOrderService.startWorkOrder(id);
    }
    
    /**
     * 完成工单
     */
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:complete')")
    public R<?> complete(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        String qualityComment = params.get("qualityComment") != null ? params.get("qualityComment").toString() : null;
        java.math.BigDecimal qualityScore = params.get("qualityScore") != null ? 
            new java.math.BigDecimal(params.get("qualityScore").toString()) : null;
        return workOrderService.completeWorkOrder(id, qualityComment, qualityScore);
    }
    
    /**
     * 更新工单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:edit')")
    public R<?> update(@RequestBody MaintenanceWorkOrder workOrder) {
        return R.success(workOrderService.updateById(workOrder));
    }
    
    /**
     * 删除工单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'maintenance:workorder:remove')")
    public R<?> delete(@PathVariable Long id) {
        return R.success(workOrderService.removeById(id));
    }
    
    // 转换方法
    private MaintenanceWorkOrder convertToWorkOrder(Map<String, Object> map) {
        MaintenanceWorkOrder workOrder = new MaintenanceWorkOrder();
        if (map.get("workOrderId") != null) workOrder.setWorkOrderId(Long.valueOf(map.get("workOrderId").toString()));
        if (map.get("workOrderNo") != null) workOrder.setWorkOrderNo(map.get("workOrderNo").toString());
        if (map.get("workOrderType") != null) workOrder.setWorkOrderType(map.get("workOrderType").toString());
        if (map.get("priority") != null) workOrder.setPriority(map.get("priority").toString());
        if (map.get("assetId") != null) workOrder.setAssetId(Long.valueOf(map.get("assetId").toString()));
        if (map.get("assetCode") != null) workOrder.setAssetCode(map.get("assetCode").toString());
        if (map.get("assetName") != null) workOrder.setAssetName(map.get("assetName").toString());
        if (map.get("faultType") != null) workOrder.setFaultType(map.get("faultType").toString());
        if (map.get("faultDescription") != null) workOrder.setFaultDescription(map.get("faultDescription").toString());
        if (map.get("reportedBy") != null) workOrder.setReportedBy(Long.valueOf(map.get("reportedBy").toString()));
        if (map.get("reportedByName") != null) workOrder.setReportedByName(map.get("reportedByName").toString());
        if (map.get("status") != null) workOrder.setStatus(map.get("status").toString());
        if (map.get("remark") != null) workOrder.setRemark(map.get("remark").toString());
        return workOrder;
    }
    
    private MaintenanceWorkOrderDetail convertToDetail(Map<String, Object> map) {
        MaintenanceWorkOrderDetail detail = new MaintenanceWorkOrderDetail();
        if (map.get("stepNo") != null) detail.setStepNo(Integer.valueOf(map.get("stepNo").toString()));
        if (map.get("stepName") != null) detail.setStepName(map.get("stepName").toString());
        if (map.get("stepDescription") != null) detail.setStepDescription(map.get("stepDescription").toString());
        if (map.get("technicianId") != null) detail.setTechnicianId(Long.valueOf(map.get("technicianId").toString()));
        if (map.get("technicianName") != null) detail.setTechnicianName(map.get("technicianName").toString());
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

