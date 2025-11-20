package com.coal.erp.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.purchase.PurchaseOrderDetail;
import com.coal.erp.business.domain.purchase.PurchaseRequisition;
import com.coal.erp.business.domain.purchase.PurchaseRequisitionDetail;
import com.coal.erp.business.domain.WarningAlert;
import com.coal.erp.business.mapper.PurchaseOrderMapper;
import com.coal.erp.business.mapper.purchase.PurchaseOrderDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseRequisitionDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseRequisitionMapper;
import com.coal.erp.business.service.IPurchaseOrderService;
import com.coal.erp.business.service.IWarningAlertService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购订单服务实现
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> 
        implements IPurchaseOrderService {
    
    @Autowired
    private PurchaseOrderDetailMapper orderDetailMapper;
    
    @Autowired
    private PurchaseRequisitionMapper requisitionMapper;
    
    @Autowired
    private PurchaseRequisitionDetailMapper requisitionDetailMapper;
    
    @Autowired
    private IWarningAlertService warningAlertService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createOrder(PurchaseOrder order, List<PurchaseOrderDetail> details) {
        // 生成订单编号
        order.setOrderNo("CG" + System.currentTimeMillis());
        order.setStatus("DRAFT");
        order.setCreateUserId(SecurityUtils.getUserId());
        order.setCreateUserName(SecurityUtils.getUsername());
        order.setCreateTime(new Date());
        if (order.getOrderDate() == null) {
            order.setOrderDate(new Date());
        }
        
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal totalAmountWithTax = BigDecimal.ZERO;
        
        for (PurchaseOrderDetail detail : details) {
            if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                detail.setAmount(detail.getUnitPrice().multiply(detail.getQuantity()));
                totalAmount = totalAmount.add(detail.getAmount());
                
                if (detail.getTaxRate() != null) {
                    detail.setTaxAmount(detail.getAmount()
                        .multiply(detail.getTaxRate())
                        .divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                    taxAmount = taxAmount.add(detail.getTaxAmount());
                    detail.setAmountWithTax(detail.getAmount().add(detail.getTaxAmount()));
                } else {
                    detail.setTaxAmount(BigDecimal.ZERO);
                    detail.setAmountWithTax(detail.getAmount());
                }
                totalAmountWithTax = totalAmountWithTax.add(detail.getAmountWithTax());
            }
        }
        
        order.setTotalAmount(totalAmount);
        order.setTaxAmount(taxAmount);
        order.setTotalAmountWithTax(totalAmountWithTax);
        
        // 保存订单
        save(order);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setOrderId(order.getOrderId());
            orderDetailMapper.insert(detail);
        });
        
        return R.success(order);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> submitOrder(Long orderId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        if (!"DRAFT".equals(order.getStatus())) {
            return R.error("只能提交草稿状态的订单");
        }
        order.setStatus("SUBMITTED");
        order.setUpdateTime(new Date());
        updateById(order);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> approveOrder(Long orderId, String approveRemark) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        if (!"SUBMITTED".equals(order.getStatus())) {
            return R.error("只能审批已提交的订单");
        }
        order.setStatus("APPROVED");
        order.setApproveUserId(SecurityUtils.getUserId());
        order.setApproveUserName(SecurityUtils.getUsername());
        order.setApproveTime(new Date());
        order.setUpdateTime(new Date());
        updateById(order);
        return R.success();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmOrder(Long orderId) {
        PurchaseOrder order = getById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        if (!"APPROVED".equals(order.getStatus())) {
            return R.error("只能确认已审批的订单");
        }
        order.setStatus("CONFIRMED");
        order.setUpdateTime(new Date());
        updateById(order);
        
        // 与预警中心集成：检查订单是否即将超期
        checkOrderDeliveryWarning(order);
        
        return R.success();
    }
    
    @Override
    public Page<PurchaseOrder> pageOrder(Long current, Long size, String orderNo, String status, Long supplierId) {
        Page<PurchaseOrder> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<>();
        if (orderNo != null && !orderNo.isEmpty()) {
            wrapper.like(PurchaseOrder::getOrderNo, orderNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseOrder::getStatus, status);
        }
        if (supplierId != null) {
            wrapper.eq(PurchaseOrder::getSupplierId, supplierId);
        }
        wrapper.orderByDesc(PurchaseOrder::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchaseOrderDetail> getOrderDetails(Long orderId) {
        LambdaQueryWrapper<PurchaseOrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseOrderDetail::getOrderId, orderId);
        return orderDetailMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createOrderFromRequisition(Long requisitionId, Long supplierId) {
        PurchaseRequisition requisition = requisitionMapper.selectById(requisitionId);
        if (requisition == null) {
            return R.error("申请不存在");
        }
        if (!"APPROVED".equals(requisition.getStatus())) {
            return R.error("只能从已审批的申请创建订单");
        }
        
        // 查询申请明细
        LambdaQueryWrapper<PurchaseRequisitionDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PurchaseRequisitionDetail::getRequisitionId, requisitionId);
        List<PurchaseRequisitionDetail> requisitionDetails = requisitionDetailMapper.selectList(detailWrapper);
        
        if (requisitionDetails.isEmpty()) {
            return R.error("申请没有明细");
        }
        
        // 创建订单
        PurchaseOrder order = new PurchaseOrder();
        order.setRequisitionId(requisitionId);
        order.setRequisitionNo(requisition.getRequisitionNo());
        order.setSupplierId(supplierId);
        order.setOrderDate(new Date());
        order.setStatus("DRAFT");
        order.setCreateUserId(SecurityUtils.getUserId());
        order.setCreateUserName(SecurityUtils.getUsername());
        order.setCreateTime(new Date());
        
        // 创建订单明细
        List<PurchaseOrderDetail> orderDetails = requisitionDetails.stream()
            .map(reqDetail -> {
                PurchaseOrderDetail orderDetail = new PurchaseOrderDetail();
                orderDetail.setItemName(reqDetail.getItemName());
                orderDetail.setItemCode(reqDetail.getItemCode());
                orderDetail.setSpecification(reqDetail.getSpecification());
                orderDetail.setBrand(reqDetail.getBrand());
                orderDetail.setUnit(reqDetail.getUnit());
                orderDetail.setQuantity(reqDetail.getQuantity());
                orderDetail.setUnitPrice(reqDetail.getEstimatedPrice());
                orderDetail.setRequiredDate(reqDetail.getRequiredDate());
                return orderDetail;
            })
            .collect(Collectors.toList());
        
        return createOrder(order, orderDetails);
    }
    
    /**
     * 检查订单交货预警（与预警中心集成）
     */
    private void checkOrderDeliveryWarning(PurchaseOrder order) {
        if (order.getDeliveryDate() == null) {
            return;
        }
        
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 3); // 交货日期前3天预警
        Date warningDate = cal.getTime();
        
        // 如果交货日期在3天内，创建预警
        if (order.getDeliveryDate().before(warningDate) || order.getDeliveryDate().equals(warningDate)) {
            long daysDiff = (order.getDeliveryDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            String alertLevel = daysDiff < 0 ? "RED" : (daysDiff <= 1 ? "ORANGE" : "YELLOW");
            
            WarningAlert alert = new WarningAlert();
            alert.setAlertType("ORDER_OVERDUE");
            alert.setAlertLevel(alertLevel);
            alert.setAlertTitle("采购订单交货预警");
            alert.setAlertContent(String.format("订单号：%s，供应商：%s，交货日期：%s，剩余%d天",
                order.getOrderNo(), order.getSupplierName(), order.getDeliveryDate(), daysDiff));
            alert.setExpireDate(order.getDeliveryDate());
            alert.setDaysRemaining((int) daysDiff);
            alert.setCreateUserId(SecurityUtils.getUserId());
            alert.setCreateTime(now);
            
            warningAlertService.createAlert(alert);
        }
    }
}
