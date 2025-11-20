package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.purchase.PurchaseRequisition;
import com.coal.erp.business.domain.purchase.PurchaseRequisitionDetail;
import com.coal.erp.business.mapper.purchase.PurchaseRequisitionDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseRequisitionMapper;
import com.coal.erp.business.service.purchase.IPurchaseRequisitionService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购申请服务实现
 */
@Service
public class PurchaseRequisitionServiceImpl extends ServiceImpl<PurchaseRequisitionMapper, PurchaseRequisition> 
        implements IPurchaseRequisitionService {
    
    @Autowired
    private PurchaseRequisitionDetailMapper requisitionDetailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createRequisition(PurchaseRequisition requisition, List<PurchaseRequisitionDetail> details) {
        // 生成申请单号
        requisition.setRequisitionNo("SQ" + System.currentTimeMillis());
        requisition.setStatus("DRAFT");
        requisition.setApplicantId(SecurityUtils.getUserId());
        requisition.setApplicantName(SecurityUtils.getUsername());
        requisition.setCreateTime(new Date());
        
        // 计算总金额
        BigDecimal totalAmount = details.stream()
            .map(detail -> {
                if (detail.getEstimatedPrice() != null && detail.getQuantity() != null) {
                    detail.setEstimatedAmount(detail.getEstimatedPrice()
                        .multiply(detail.getQuantity()));
                    return detail.getEstimatedAmount();
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        requisition.setTotalAmount(totalAmount);
        
        // 保存申请
        save(requisition);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setRequisitionId(requisition.getRequisitionId());
            requisitionDetailMapper.insert(detail);
        });
        
        return R.success(requisition);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitRequisition(Long requisitionId) {
        PurchaseRequisition requisition = getById(requisitionId);
        if (requisition == null) {
            return R.error("申请不存在");
        }
        if (!"DRAFT".equals(requisition.getStatus())) {
            return R.error("只能提交草稿状态的申请");
        }
        requisition.setStatus("SUBMITTED");
        requisition.setUpdateTime(new Date());
        updateById(requisition);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveRequisition(Long requisitionId, String approveRemark) {
        PurchaseRequisition requisition = getById(requisitionId);
        if (requisition == null) {
            return R.error("申请不存在");
        }
        if (!"SUBMITTED".equals(requisition.getStatus()) && !"APPROVING".equals(requisition.getStatus())) {
            return R.error("只能审批已提交的申请");
        }
        requisition.setStatus("APPROVED");
        requisition.setApproveUserId(SecurityUtils.getUserId());
        requisition.setApproveUserName(SecurityUtils.getUsername());
        requisition.setApproveTime(new Date());
        requisition.setApproveRemark(approveRemark);
        requisition.setUpdateTime(new Date());
        updateById(requisition);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> rejectRequisition(Long requisitionId, String approveRemark) {
        PurchaseRequisition requisition = getById(requisitionId);
        if (requisition == null) {
            return R.error("申请不存在");
        }
        if (!"SUBMITTED".equals(requisition.getStatus()) && !"APPROVING".equals(requisition.getStatus())) {
            return R.error("只能驳回已提交的申请");
        }
        requisition.setStatus("REJECTED");
        requisition.setApproveUserId(SecurityUtils.getUserId());
        requisition.setApproveUserName(SecurityUtils.getUsername());
        requisition.setApproveTime(new Date());
        requisition.setApproveRemark(approveRemark);
        requisition.setUpdateTime(new Date());
        updateById(requisition);
        return R.success();
    }
    
    @Override
    public Page<PurchaseRequisition> pageRequisition(Long current, Long size, String requisitionNo, String status) {
        Page<PurchaseRequisition> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseRequisition> wrapper = new LambdaQueryWrapper<>();
        if (requisitionNo != null && !requisitionNo.isEmpty()) {
            wrapper.like(PurchaseRequisition::getRequisitionNo, requisitionNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseRequisition::getStatus, status);
        }
        wrapper.orderByDesc(PurchaseRequisition::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchaseRequisitionDetail> getRequisitionDetails(Long requisitionId) {
        LambdaQueryWrapper<PurchaseRequisitionDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseRequisitionDetail::getRequisitionId, requisitionId);
        return requisitionDetailMapper.selectList(wrapper);
    }
}

