package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.finance.FinancePayment;
import com.coal.erp.business.domain.finance.FinancePayable;
import com.coal.erp.business.domain.purchase.PurchaseOrderDetail;
import com.coal.erp.business.domain.purchase.PurchasePayment;
import com.coal.erp.business.domain.purchase.PurchasePaymentDetail;
import com.coal.erp.business.mapper.PurchaseOrderMapper;
import com.coal.erp.business.mapper.finance.FinancePayableMapper;
import com.coal.erp.business.mapper.purchase.PurchaseOrderDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchasePaymentDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchasePaymentMapper;
import com.coal.erp.business.service.finance.IFinancePaymentService;
import com.coal.erp.business.service.finance.IFinancePayableService;
import com.coal.erp.business.service.purchase.IPurchasePaymentService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购付款服务实现
 */
@Service
public class PurchasePaymentServiceImpl extends ServiceImpl<PurchasePaymentMapper, PurchasePayment> 
        implements IPurchasePaymentService {
    
    @Autowired
    private PurchasePaymentDetailMapper paymentDetailMapper;
    
    @Autowired
    private PurchaseOrderMapper orderMapper;
    
    @Autowired
    private PurchaseOrderDetailMapper orderDetailMapper;
    
    @Autowired
    private IFinancePaymentService financePaymentService;
    
    @Autowired
    private IFinancePayableService financePayableService;
    
    @Autowired
    private FinancePayableMapper financePayableMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createPayment(PurchasePayment payment, List<PurchasePaymentDetail> details) {
        // 生成付款单号
        payment.setPaymentNo("FK" + System.currentTimeMillis());
        payment.setStatus("DRAFT");
        payment.setPaymentDate(new Date());
        payment.setCreateUserId(SecurityUtils.getUserId());
        payment.setCreateUserName(SecurityUtils.getUsername());
        payment.setCreateTime(new Date());
        
        // 计算付款金额
        BigDecimal paymentAmount = details.stream()
            .map(PurchasePaymentDetail::getPaymentAmount)
            .filter(amount -> amount != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        payment.setPaymentAmount(paymentAmount);
        
        // 保存付款单
        save(payment);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setPaymentId(payment.getPaymentId());
            paymentDetailMapper.insert(detail);
        });
        
        return R.success(payment);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitPayment(Long paymentId) {
        PurchasePayment payment = getById(paymentId);
        if (payment == null) {
            return R.error("付款单不存在");
        }
        if (!"DRAFT".equals(payment.getStatus())) {
            return R.error("只能提交草稿状态的付款单");
        }
        payment.setStatus("SUBMITTED");
        payment.setUpdateTime(new Date());
        updateById(payment);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approvePayment(Long paymentId, String approveRemark) {
        PurchasePayment payment = getById(paymentId);
        if (payment == null) {
            return R.error("付款单不存在");
        }
        if (!"SUBMITTED".equals(payment.getStatus())) {
            return R.error("只能审批已提交的付款单");
        }
        payment.setStatus("APPROVED");
        payment.setApproveUserId(SecurityUtils.getUserId());
        payment.setApproveUserName(SecurityUtils.getUsername());
        payment.setApproveTime(new Date());
        payment.setUpdateTime(new Date());
        updateById(payment);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmPayment(Long paymentId) {
        PurchasePayment payment = getById(paymentId);
        if (payment == null) {
            return R.error("付款单不存在");
        }
        if (!"APPROVED".equals(payment.getStatus())) {
            return R.error("只能确认已审批的付款单");
        }
        payment.setStatus("PAID");
        payment.setPayUserId(SecurityUtils.getUserId());
        payment.setPayUserName(SecurityUtils.getUsername());
        payment.setPayTime(new Date());
        payment.setUpdateTime(new Date());
        updateById(payment);
        
        // 与财务付款流程集成：自动创建财务付款单
        createFinancePaymentFromPurchase(payment);
        
        return R.success();
    }
    
    @Override
    public Page<PurchasePayment> pagePayment(Long current, Long size, String paymentNo, String status) {
        Page<PurchasePayment> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchasePayment> wrapper = new LambdaQueryWrapper<>();
        if (paymentNo != null && !paymentNo.isEmpty()) {
            wrapper.like(PurchasePayment::getPaymentNo, paymentNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchasePayment::getStatus, status);
        }
        wrapper.orderByDesc(PurchasePayment::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchasePaymentDetail> getPaymentDetails(Long paymentId) {
        LambdaQueryWrapper<PurchasePaymentDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchasePaymentDetail::getPaymentId, paymentId);
        return paymentDetailMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createPaymentFromOrder(Long orderId, String paymentType) {
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
        
        // 创建付款单
        PurchasePayment payment = new PurchasePayment();
        payment.setOrderId(orderId);
        payment.setOrderNo(order.getOrderNo());
        payment.setSupplierId(order.getSupplierId());
        payment.setSupplierName(order.getSupplierName());
        payment.setPaymentType(paymentType);
        payment.setPaymentDate(new Date());
        payment.setOrderAmount(order.getTotalAmountWithTax());
        payment.setPaidAmount(BigDecimal.ZERO);
        payment.setBalanceAmount(order.getTotalAmountWithTax());
        payment.setStatus("DRAFT");
        payment.setCreateUserId(SecurityUtils.getUserId());
        payment.setCreateUserName(SecurityUtils.getUsername());
        payment.setCreateTime(new Date());
        
        // 创建付款明细
        List<PurchasePaymentDetail> paymentDetails = orderDetails.stream()
            .map(orderDetail -> {
                PurchasePaymentDetail detail = new PurchasePaymentDetail();
                detail.setOrderId(orderId);
                detail.setOrderNo(order.getOrderNo());
                detail.setItemName(orderDetail.getItemName());
                detail.setPaymentAmount(orderDetail.getAmountWithTax());
                return detail;
            })
            .collect(Collectors.toList());
        
        return createPayment(payment, paymentDetails);
    }
    
    /**
     * 从采购付款创建财务付款单（与财务付款流程集成）
     */
    private void createFinancePaymentFromPurchase(PurchasePayment purchasePayment) {
        try {
            // 创建财务付款单
            FinancePayment financePayment = new FinancePayment();
            financePayment.setPaymentNo(purchasePayment.getPaymentNo());
            financePayment.setPaymentType("PAY"); // 付款
            financePayment.setSupplierId(purchasePayment.getSupplierId());
            financePayment.setPaymentDate(purchasePayment.getPaymentDate());
            financePayment.setAmount(purchasePayment.getPaymentAmount());
            financePayment.setCurrency(purchasePayment.getCurrency());
            financePayment.setPaymentMethod(purchasePayment.getPaymentMethod());
            financePayment.setBankAccount(purchasePayment.getBankAccount());
            financePayment.setStatus("UNCONFIRMED");
            financePayment.setCreateTime(new Date());
            financePayment.setDescription("来自采购付款单：" + purchasePayment.getPaymentNo());
            
            // 查询或创建应付单据
            FinancePayable payable = financePayableMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FinancePayable>()
                    .eq(FinancePayable::getSupplierId, purchasePayment.getSupplierId())
                    .eq(FinancePayable::getStatus, "UNPAID")
                    .orderByDesc(FinancePayable::getCreateTime)
                    .last("LIMIT 1")
            );
            
            List<Long> payableIds = new ArrayList<>();
            if (payable != null) {
                payableIds.add(payable.getPayableId());
            }
            
            // 创建财务付款单
            financePaymentService.createPayPayment(financePayment, payableIds);
        } catch (Exception e) {
            // 记录日志但不影响采购付款确认流程
            System.err.println("创建财务付款单失败：" + e.getMessage());
        }
    }
}

