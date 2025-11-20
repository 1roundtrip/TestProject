package com.coal.erp.business.service.maintenance.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.maintenance.MaintenancePartRequisition;
import com.coal.erp.business.domain.maintenance.MaintenancePartRequisitionDetail;
import com.coal.erp.business.mapper.maintenance.MaintenancePartRequisitionDetailMapper;
import com.coal.erp.business.mapper.maintenance.MaintenancePartRequisitionMapper;
import com.coal.erp.business.service.maintenance.IMaintenancePartRequisitionService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 维修备件领用服务实现
 */
@Service
public class MaintenancePartRequisitionServiceImpl extends ServiceImpl<MaintenancePartRequisitionMapper, MaintenancePartRequisition> 
        implements IMaintenancePartRequisitionService {
    
    @Autowired
    private MaintenancePartRequisitionDetailMapper detailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createRequisition(MaintenancePartRequisition requisition, List<MaintenancePartRequisitionDetail> details) {
        try {
            if (requisition.getRequisitionNo() == null || requisition.getRequisitionNo().isEmpty()) {
                requisition.setRequisitionNo("PR" + System.currentTimeMillis());
            }
            requisition.setStatus("PENDING");
            requisition.setCreateTime(new Date());
            requisition.setUpdateTime(new Date());
            save(requisition);
            
            if (details != null && !details.isEmpty()) {
                java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
                for (MaintenancePartRequisitionDetail detail : details) {
                    detail.setRequisitionId(requisition.getRequisitionId());
                    detail.setAmount(detail.getQuantity().multiply(detail.getUnitPrice()));
                    totalAmount = totalAmount.add(detail.getAmount());
                    detailMapper.insert(detail);
                }
                requisition.setTotalAmount(totalAmount);
                updateById(requisition);
            }
            
            return R.success(requisition);
        } catch (Exception e) {
            return R.error("创建领用单失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<MaintenancePartRequisition> pageRequisition(Long current, Long size, String requisitionNo, String status) {
        Page<MaintenancePartRequisition> page = new Page<>(current, size);
        LambdaQueryWrapper<MaintenancePartRequisition> wrapper = new LambdaQueryWrapper<>();
        if (requisitionNo != null && !requisitionNo.isEmpty()) {
            wrapper.like(MaintenancePartRequisition::getRequisitionNo, requisitionNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(MaintenancePartRequisition::getStatus, status);
        }
        wrapper.orderByDesc(MaintenancePartRequisition::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveRequisition(Long requisitionId) {
        try {
            MaintenancePartRequisition requisition = getById(requisitionId);
            if (requisition == null) {
                return R.error("领用单不存在");
            }
            if (!"PENDING".equals(requisition.getStatus())) {
                return R.error("领用单状态不允许审批");
            }
            requisition.setStatus("APPROVED");
            requisition.setApproveTime(new Date());
            requisition.setUpdateTime(new Date());
            updateById(requisition);
            return R.success();
        } catch (Exception e) {
            return R.error("审批失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> issueRequisition(Long requisitionId) {
        try {
            MaintenancePartRequisition requisition = getById(requisitionId);
            if (requisition == null) {
                return R.error("领用单不存在");
            }
            if (!"APPROVED".equals(requisition.getStatus())) {
                return R.error("领用单状态不允许发放");
            }
            requisition.setStatus("ISSUED");
            requisition.setIssueTime(new Date());
            requisition.setUpdateTime(new Date());
            updateById(requisition);
            // TODO: 扣减库存
            return R.success();
        } catch (Exception e) {
            return R.error("发放失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<MaintenancePartRequisitionDetail> getRequisitionDetails(Long requisitionId) {
        LambdaQueryWrapper<MaintenancePartRequisitionDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaintenancePartRequisitionDetail::getRequisitionId, requisitionId);
        return detailMapper.selectList(wrapper);
    }
}

