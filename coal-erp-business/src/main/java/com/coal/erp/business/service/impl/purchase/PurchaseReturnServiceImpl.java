package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.purchase.PurchaseReturn;
import com.coal.erp.business.domain.purchase.PurchaseReturnDetail;
import com.coal.erp.business.mapper.purchase.PurchaseReturnDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReturnMapper;
import com.coal.erp.business.service.purchase.IPurchaseReturnService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购退货服务实现
 */
@Service
public class PurchaseReturnServiceImpl extends ServiceImpl<PurchaseReturnMapper, PurchaseReturn> 
        implements IPurchaseReturnService {
    
    @Autowired
    private PurchaseReturnDetailMapper returnDetailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createReturn(PurchaseReturn returnOrder, List<PurchaseReturnDetail> details) {
        // 生成退货单号
        returnOrder.setReturnNo("TH" + System.currentTimeMillis());
        returnOrder.setStatus("DRAFT");
        returnOrder.setReturnDate(new Date());
        returnOrder.setCreateUserId(SecurityUtils.getUserId());
        returnOrder.setCreateUserName(SecurityUtils.getUsername());
        returnOrder.setCreateTime(new Date());
        
        // 计算总金额
        BigDecimal totalAmount = details.stream()
            .map(detail -> {
                if (detail.getUnitPrice() != null && detail.getReturnQuantity() != null) {
                    detail.setTotalAmount(detail.getUnitPrice()
                        .multiply(detail.getReturnQuantity()));
                    return detail.getTotalAmount();
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        returnOrder.setTotalAmount(totalAmount);
        
        // 保存退货单
        save(returnOrder);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setReturnId(returnOrder.getReturnId());
            returnDetailMapper.insert(detail);
        });
        
        return R.success(returnOrder);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitReturn(Long returnId) {
        PurchaseReturn returnOrder = getById(returnId);
        if (returnOrder == null) {
            return R.error("退货单不存在");
        }
        if (!"DRAFT".equals(returnOrder.getStatus())) {
            return R.error("只能提交草稿状态的退货单");
        }
        returnOrder.setStatus("SUBMITTED");
        returnOrder.setUpdateTime(new Date());
        updateById(returnOrder);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveReturn(Long returnId, String approveRemark) {
        PurchaseReturn returnOrder = getById(returnId);
        if (returnOrder == null) {
            return R.error("退货单不存在");
        }
        if (!"SUBMITTED".equals(returnOrder.getStatus())) {
            return R.error("只能审批已提交的退货单");
        }
        returnOrder.setStatus("APPROVED");
        returnOrder.setApproveUserId(SecurityUtils.getUserId());
        returnOrder.setApproveUserName(SecurityUtils.getUsername());
        returnOrder.setApproveTime(new Date());
        returnOrder.setUpdateTime(new Date());
        updateById(returnOrder);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmReturn(Long returnId) {
        PurchaseReturn returnOrder = getById(returnId);
        if (returnOrder == null) {
            return R.error("退货单不存在");
        }
        if (!"APPROVED".equals(returnOrder.getStatus())) {
            return R.error("只能确认已审批的退货单");
        }
        returnOrder.setStatus("RETURNING");
        returnOrder.setReturnUserId(SecurityUtils.getUserId());
        returnOrder.setReturnUserName(SecurityUtils.getUsername());
        returnOrder.setUpdateTime(new Date());
        updateById(returnOrder);
        return R.success();
    }
    
    @Override
    public Page<PurchaseReturn> pageReturn(Long current, Long size, String returnNo, String status) {
        Page<PurchaseReturn> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseReturn> wrapper = new LambdaQueryWrapper<>();
        if (returnNo != null && !returnNo.isEmpty()) {
            wrapper.like(PurchaseReturn::getReturnNo, returnNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseReturn::getStatus, status);
        }
        wrapper.orderByDesc(PurchaseReturn::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchaseReturnDetail> getReturnDetails(Long returnId) {
        LambdaQueryWrapper<PurchaseReturnDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseReturnDetail::getReturnId, returnId);
        return returnDetailMapper.selectList(wrapper);
    }
}

