package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrder;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrderDetail;
import com.coal.erp.business.mapper.maintenance.MaintenanceWorkOrderDetailMapper;
import com.coal.erp.business.mapper.maintenance.MaintenanceWorkOrderMapper;
import com.coal.erp.business.service.maintenance.IMaintenanceWorkOrderService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 维修工单服务实现
 */
@Service
public class MaintenanceWorkOrderServiceImpl extends ServiceImpl<MaintenanceWorkOrderMapper, MaintenanceWorkOrder> 
        implements IMaintenanceWorkOrderService {
    
    @Autowired
    private MaintenanceWorkOrderDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createWorkOrder(MaintenanceWorkOrder workOrder, List<MaintenanceWorkOrderDetail> details) {
        try {
            // 生成工单编号
            if (workOrder.getWorkOrderNo() == null || workOrder.getWorkOrderNo().isEmpty()) {
                workOrder.setWorkOrderNo("WO" + System.currentTimeMillis());
            }
            
            workOrder.setStatus("PENDING");
            workOrder.setCreateTime(new Date());
            workOrder.setUpdateTime(new Date());
            
            // 保存工单
            save(workOrder);
            
            // 保存明细
            if (details != null && !details.isEmpty()) {
                for (MaintenanceWorkOrderDetail detail : details) {
                    detail.setWorkOrderId(workOrder.getWorkOrderId());
                    detail.setStatus("PENDING");
                    detailMapper.insert(detail);
                }
            }
            
            return R.success(workOrder);
        } catch (Exception e) {
            return R.error("创建工单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenanceWorkOrder> pageWorkOrder(Long current, Long size, String workOrderNo, String status, Long assetId) {
        Page<MaintenanceWorkOrder> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenanceWorkOrder> wrapper = new LambdaQueryWrapper<>();
        
        if (workOrderNo != null && !workOrderNo.isEmpty()) {
            wrapper.like(MaintenanceWorkOrder::getWorkOrderNo, workOrderNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MaintenanceWorkOrder::getStatus, status);
        }
        if (assetId != null) {
            wrapper.eq(MaintenanceWorkOrder::getAssetId, assetId);
        }
        
        wrapper.orderByDesc(MaintenanceWorkOrder::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> assignWorkOrder(Long workOrderId, Long teamId, Long technicianId) {
        try {
            MaintenanceWorkOrder workOrder = getById(workOrderId);
            if (workOrder == null) {
                return R.error("工单不存在");
            }
            
            if (!"PENDING".equals(workOrder.getStatus())) {
                return R.error("工单状态不允许分配");
            }
            
            workOrder.setAssignedTeamId(teamId);
            workOrder.setAssignedTechnicianId(technicianId);
            workOrder.setStatus("ASSIGNED");
            workOrder.setUpdateTime(new Date());
            
            updateById(workOrder);
            return R.success();
        } catch (Exception e) {
            return R.error("分配工单失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> startWorkOrder(Long workOrderId) {
        try {
            MaintenanceWorkOrder workOrder = getById(workOrderId);
            if (workOrder == null) {
                return R.error("工单不存在");
            }
            
            workOrder.setStatus("IN_PROGRESS");
            workOrder.setActualStartTime(new Date());
            workOrder.setUpdateTime(new Date());
            
            updateById(workOrder);
            return R.success();
        } catch (Exception e) {
            return R.error("开始维修失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> completeWorkOrder(Long workOrderId, String qualityComment, java.math.BigDecimal qualityScore) {
        try {
            MaintenanceWorkOrder workOrder = getById(workOrderId);
            if (workOrder == null) {
                return R.error("工单不存在");
            }
            
            workOrder.setStatus("COMPLETED");
            workOrder.setActualEndTime(new Date());
            workOrder.setCompletionRate(java.math.BigDecimal.valueOf(100));
            workOrder.setQualityComment(qualityComment);
            workOrder.setQualityScore(qualityScore);
            workOrder.setUpdateTime(new Date());
            
            updateById(workOrder);
            return R.success();
        } catch (Exception e) {
            return R.error("完成工单失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<MaintenanceWorkOrderDetail> getWorkOrderDetails(Long workOrderId) {
        LambdaQueryWrapper<MaintenanceWorkOrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaintenanceWorkOrderDetail::getWorkOrderId, workOrderId);
        wrapper.orderByAsc(MaintenanceWorkOrderDetail::getStepNo);
        return detailMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateWorkOrderDetails(Long workOrderId, List<MaintenanceWorkOrderDetail> details) {
        try {
            // 删除原有明细
            LambdaQueryWrapper<MaintenanceWorkOrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MaintenanceWorkOrderDetail::getWorkOrderId, workOrderId);
            detailMapper.delete(wrapper);
            
            // 插入新明细
            if (details != null && !details.isEmpty()) {
                for (MaintenanceWorkOrderDetail detail : details) {
                    detail.setWorkOrderId(workOrderId);
                    detailMapper.insert(detail);
                }
            }
            
            return R.success();
        } catch (Exception e) {
            return R.error("更新明细失败：" + e.getMessage());
        }
    }
}

