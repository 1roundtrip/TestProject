package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.purchase.PurchaseContract;
import com.coal.erp.business.domain.purchase.PurchaseContractDetail;
import com.coal.erp.business.domain.purchase.PurchaseOrderDetail;
import com.coal.erp.business.mapper.PurchaseOrderMapper;
import com.coal.erp.business.mapper.purchase.PurchaseContractDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseContractMapper;
import com.coal.erp.business.mapper.purchase.PurchaseOrderDetailMapper;
import com.coal.erp.business.service.purchase.IPurchaseContractService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购合同服务实现
 */
@Service
public class PurchaseContractServiceImpl extends ServiceImpl<PurchaseContractMapper, PurchaseContract> 
        implements IPurchaseContractService {
    
    @Autowired
    private PurchaseContractDetailMapper contractDetailMapper;
    
    @Autowired
    private PurchaseOrderMapper orderMapper;
    
    @Autowired
    private PurchaseOrderDetailMapper orderDetailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createContract(PurchaseContract contract, List<PurchaseContractDetail> details) {
        // 生成合同编号
        contract.setContractNo("HT" + System.currentTimeMillis());
        contract.setStatus("DRAFT");
        contract.setCreateUserId(SecurityUtils.getUserId());
        contract.setCreateUserName(SecurityUtils.getUsername());
        contract.setCreateTime(new Date());
        
        // 计算总金额
        BigDecimal totalAmount = details.stream()
            .map(detail -> {
                if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                    detail.setTotalAmount(detail.getUnitPrice()
                        .multiply(detail.getQuantity()));
                    return detail.getTotalAmount();
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        contract.setTotalAmount(totalAmount);
        
        // 保存合同
        save(contract);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setContractId(contract.getContractId());
            contractDetailMapper.insert(detail);
        });
        
        return R.success(contract);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitContract(Long contractId) {
        PurchaseContract contract = getById(contractId);
        if (contract == null) {
            return R.error("合同不存在");
        }
        if (!"DRAFT".equals(contract.getStatus())) {
            return R.error("只能提交草稿状态的合同");
        }
        contract.setStatus("SUBMITTED");
        contract.setUpdateTime(new Date());
        updateById(contract);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveContract(Long contractId, String approveRemark) {
        PurchaseContract contract = getById(contractId);
        if (contract == null) {
            return R.error("合同不存在");
        }
        if (!"SUBMITTED".equals(contract.getStatus())) {
            return R.error("只能审批已提交的合同");
        }
        contract.setStatus("APPROVED");
        contract.setApproveUserId(SecurityUtils.getUserId());
        contract.setApproveUserName(SecurityUtils.getUsername());
        contract.setApproveTime(new Date());
        contract.setUpdateTime(new Date());
        updateById(contract);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> signContract(Long contractId) {
        PurchaseContract contract = getById(contractId);
        if (contract == null) {
            return R.error("合同不存在");
        }
        if (!"APPROVED".equals(contract.getStatus())) {
            return R.error("只能签订已审批的合同");
        }
        contract.setStatus("SIGNED");
        contract.setSignUserId(SecurityUtils.getUserId());
        contract.setSignUserName(SecurityUtils.getUsername());
        contract.setSignTime(new Date());
        contract.setUpdateTime(new Date());
        updateById(contract);
        return R.success();
    }
    
    @Override
    public Page<PurchaseContract> pageContract(Long current, Long size, String contractNo, String status) {
        Page<PurchaseContract> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseContract> wrapper = new LambdaQueryWrapper<>();
        if (contractNo != null && !contractNo.isEmpty()) {
            wrapper.like(PurchaseContract::getContractNo, contractNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseContract::getStatus, status);
        }
        wrapper.orderByDesc(PurchaseContract::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchaseContractDetail> getContractDetails(Long contractId) {
        LambdaQueryWrapper<PurchaseContractDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseContractDetail::getContractId, contractId);
        return contractDetailMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createContractFromOrder(Long orderId) {
        PurchaseOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        
        // 查询订单明细
        List<PurchaseOrderDetail> orderDetails = orderDetailMapper.selectList(
            new LambdaQueryWrapper<PurchaseOrderDetail>()
                .eq(PurchaseOrderDetail::getOrderId, orderId)
        );
        
        if (orderDetails.isEmpty()) {
            return R.error("订单没有明细");
        }
        
        // 创建合同
        PurchaseContract contract = new PurchaseContract();
        contract.setOrderId(orderId);
        contract.setOrderNo(order.getOrderNo());
        contract.setSupplierId(order.getSupplierId());
        contract.setSupplierName(order.getSupplierName());
        contract.setContractType("SPECIFIC");
        contract.setContractDate(new Date());
        contract.setStartDate(new Date());
        // 默认合同期限1年
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.YEAR, 1);
        contract.setEndDate(cal.getTime());
        contract.setCurrency(order.getCurrency());
        contract.setStatus("DRAFT");
        contract.setCreateUserId(SecurityUtils.getUserId());
        contract.setCreateUserName(SecurityUtils.getUsername());
        contract.setCreateTime(new Date());
        
        // 创建合同明细
        List<PurchaseContractDetail> contractDetails = orderDetails.stream()
            .map(orderDetail -> {
                PurchaseContractDetail detail = new PurchaseContractDetail();
                detail.setItemName(orderDetail.getItemName());
                detail.setItemCode(orderDetail.getItemCode());
                detail.setSpecification(orderDetail.getSpecification());
                detail.setUnit(orderDetail.getUnit());
                detail.setQuantity(orderDetail.getQuantity());
                detail.setUnitPrice(orderDetail.getUnitPrice());
                detail.setDeliveryDate(orderDetail.getRequiredDate());
                return detail;
            })
            .collect(Collectors.toList());
        
        return createContract(contract, contractDetails);
    }
}

