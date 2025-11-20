package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.asset.AssetStorage;
import com.coal.erp.business.domain.asset.AssetStorageDetail;
import com.coal.erp.business.domain.purchase.PurchaseOrderDetail;
import com.coal.erp.business.domain.purchase.PurchaseReceiving;
import com.coal.erp.business.domain.purchase.PurchaseReceivingDetail;
import com.coal.erp.business.mapper.PurchaseOrderMapper;
import com.coal.erp.business.mapper.purchase.PurchaseOrderDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReceivingDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReceivingMapper;
import com.coal.erp.business.service.asset.IAssetStorageService;
import com.coal.erp.business.service.purchase.IPurchaseReceivingService;
import com.coal.erp.business.event.BusinessEventPublisher;
import com.coal.erp.business.event.PurchaseToAssetEvent;
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
 * 采购收货服务实现
 */
@Service
public class PurchaseReceivingServiceImpl extends ServiceImpl<PurchaseReceivingMapper, PurchaseReceiving> 
        implements IPurchaseReceivingService {
    
    @Autowired
    private PurchaseReceivingDetailMapper receivingDetailMapper;
    
    @Autowired
    private PurchaseOrderMapper orderMapper;
    
    @Autowired
    private PurchaseOrderDetailMapper orderDetailMapper;
    
    @Autowired
    private IAssetStorageService assetStorageService;
    
    @Autowired
    private BusinessEventPublisher eventPublisher;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createReceiving(PurchaseReceiving receiving, List<PurchaseReceivingDetail> details) {
        // 生成收货单号
        receiving.setReceivingNo("SH" + System.currentTimeMillis());
        receiving.setStatus("DRAFT");
        receiving.setCreateUserId(SecurityUtils.getUserId());
        receiving.setCreateUserName(SecurityUtils.getUsername());
        receiving.setCreateTime(new Date());
        
        // 计算总金额
        BigDecimal totalAmount = details.stream()
            .map(detail -> {
                if (detail.getUnitPrice() != null && detail.getReceivedQuantity() != null) {
                    detail.setTotalAmount(detail.getUnitPrice()
                        .multiply(detail.getReceivedQuantity()));
                    return detail.getTotalAmount();
                }
                return BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        receiving.setTotalAmount(totalAmount);
        
        // 保存收货单
        save(receiving);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setReceivingId(receiving.getReceivingId());
            receivingDetailMapper.insert(detail);
        });
        
        return R.success(receiving);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> confirmReceiving(Long receivingId) {
        PurchaseReceiving receiving = getById(receivingId);
        if (receiving == null) {
            return R.error("收货单不存在");
        }
        if (!"DRAFT".equals(receiving.getStatus())) {
            return R.error("只能确认草稿状态的收货单");
        }
        receiving.setStatus("CONFIRMED");
        receiving.setReceiverId(SecurityUtils.getUserId());
        receiving.setReceiverName(SecurityUtils.getUsername());
        receiving.setUpdateTime(new Date());
        updateById(receiving);
        
        // 更新订单已收货数量
        if (receiving.getOrderId() != null) {
            updateOrderReceivedQuantity(receiving.getOrderId());
        }
        
        // 发布采购收货确认事件，触发业务流程集成
        PurchaseToAssetEvent event = new PurchaseToAssetEvent();
        event.setOrderId(receiving.getOrderId());
        event.setOrderNo(receiving.getOrderNo());
        event.setReceivingId(receivingId);
        event.setReceivingNo(receiving.getReceivingNo());
        event.setSupplierId(receiving.getSupplierId());
        event.setSupplierName(receiving.getSupplierName());
        event.setStep("RECEIVING_CONFIRMED");
        event.setBusinessId(receivingId);
        event.setBusinessNo(receiving.getReceivingNo());
        event.setCreateUserId(SecurityUtils.getUserId());
        event.setCreateUserName(SecurityUtils.getUsername());
        
        eventPublisher.publishPurchaseToAssetEvent(event);
        
        return R.success();
    }
    
    @Override
    public Page<PurchaseReceiving> pageReceiving(Long current, Long size, String receivingNo, String status) {
        Page<PurchaseReceiving> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseReceiving> wrapper = new LambdaQueryWrapper<>();
        if (receivingNo != null && !receivingNo.isEmpty()) {
            wrapper.like(PurchaseReceiving::getReceivingNo, receivingNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseReceiving::getStatus, status);
        }
        wrapper.orderByDesc(PurchaseReceiving::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchaseReceivingDetail> getReceivingDetails(Long receivingId) {
        LambdaQueryWrapper<PurchaseReceivingDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseReceivingDetail::getReceivingId, receivingId);
        return receivingDetailMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createReceivingFromOrder(Long orderId) {
        PurchaseOrder order = orderMapper.selectById(orderId);
        if (order == null) {
            return R.error("订单不存在");
        }
        
        // 查询订单明细
        LambdaQueryWrapper<PurchaseOrderDetail> orderDetailWrapper = new LambdaQueryWrapper<>();
        orderDetailWrapper.eq(PurchaseOrderDetail::getOrderId, orderId);
        List<PurchaseOrderDetail> orderDetails = orderDetailMapper.selectList(orderDetailWrapper);
        
        if (orderDetails.isEmpty()) {
            return R.error("订单没有明细");
        }
        
        // 创建收货单
        PurchaseReceiving receiving = new PurchaseReceiving();
        receiving.setOrderId(orderId);
        receiving.setOrderNo(order.getOrderNo());
        receiving.setSupplierId(order.getSupplierId());
        receiving.setSupplierName(order.getSupplierName());
        receiving.setReceivingDate(new Date());
        receiving.setStatus("DRAFT");
        receiving.setCreateUserId(SecurityUtils.getUserId());
        receiving.setCreateUserName(SecurityUtils.getUsername());
        receiving.setCreateTime(new Date());
        
        // 创建收货明细
        List<PurchaseReceivingDetail> receivingDetails = orderDetails.stream()
            .map(orderDetail -> {
                PurchaseReceivingDetail detail = new PurchaseReceivingDetail();
                detail.setOrderDetailId(orderDetail.getDetailId());
                detail.setItemName(orderDetail.getItemName());
                detail.setItemCode(orderDetail.getItemCode());
                detail.setSpecification(orderDetail.getSpecification());
                detail.setUnit(orderDetail.getUnit());
                detail.setOrderQuantity(orderDetail.getQuantity());
                detail.setReceivedQuantity(BigDecimal.ZERO);
                detail.setUnitPrice(orderDetail.getUnitPrice());
                detail.setQualityStatus("PENDING");
                detail.setStorageStatus("PENDING");
                return detail;
            })
            .collect(Collectors.toList());
        
        return createReceiving(receiving, receivingDetails);
    }
    
    /**
     * 更新订单已收货数量
     */
    private void updateOrderReceivedQuantity(Long orderId) {
        // 查询该订单的所有收货明细
        LambdaQueryWrapper<PurchaseReceiving> receivingWrapper = new LambdaQueryWrapper<>();
        receivingWrapper.eq(PurchaseReceiving::getOrderId, orderId);
        receivingWrapper.in(PurchaseReceiving::getStatus, 
            java.util.Arrays.asList("CONFIRMED", "QUALITY_PASSED", "STORED"));
        List<PurchaseReceiving> receivings = list(receivingWrapper);
        
        // 汇总已收货数量
        for (PurchaseReceiving receiving : receivings) {
            List<PurchaseReceivingDetail> details = getReceivingDetails(receiving.getReceivingId());
            for (PurchaseReceivingDetail detail : details) {
                if (detail.getOrderDetailId() != null && detail.getReceivedQuantity() != null) {
                    PurchaseOrderDetail orderDetail = orderDetailMapper.selectById(detail.getOrderDetailId());
                    if (orderDetail != null) {
                        BigDecimal receivedQty = orderDetail.getReceivedQuantity() != null 
                            ? orderDetail.getReceivedQuantity() : BigDecimal.ZERO;
                        orderDetail.setReceivedQuantity(receivedQty.add(detail.getReceivedQuantity()));
                        orderDetailMapper.updateById(orderDetail);
                    }
                }
            }
        }
    }
    
    /**
     * 从采购收货创建资产入库单（与资产入库流程集成）
     * 注意：此方法已被事件驱动方式替代，保留用于向后兼容
     */
    @SuppressWarnings("unused")
    private void createAssetStorageFromReceiving(PurchaseReceiving receiving) {
        try {
            // 查询收货明细
            List<PurchaseReceivingDetail> receivingDetails = getReceivingDetails(receiving.getReceivingId());
            
            // 创建资产入库单
            AssetStorage assetStorage = new AssetStorage();
            assetStorage.setStorageType("PURCHASE");
            assetStorage.setStorageDate(receiving.getReceivingDate());
            assetStorage.setSupplierId(receiving.getSupplierId());
            assetStorage.setSupplierName(receiving.getSupplierName());
            assetStorage.setWarehouse(receiving.getWarehouse());
            assetStorage.setLocation(receiving.getLocation());
            assetStorage.setStatus("DRAFT");
            assetStorage.setCreateUserId(SecurityUtils.getUserId());
            assetStorage.setCreateUserName(SecurityUtils.getUsername());
            assetStorage.setCreateTime(new Date());
            
            // 创建资产入库明细
            List<AssetStorageDetail> assetDetails = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (PurchaseReceivingDetail receivingDetail : receivingDetails) {
                if ("PASSED".equals(receivingDetail.getQualityStatus()) && 
                    receivingDetail.getQualifiedQuantity() != null &&
                    receivingDetail.getQualifiedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    
                    AssetStorageDetail detail = new AssetStorageDetail();
                    detail.setAssetName(receivingDetail.getItemName());
                    detail.setAssetCode(receivingDetail.getItemCode());
                    detail.setModel(receivingDetail.getSpecification());
                    detail.setQuantity(receivingDetail.getQualifiedQuantity().intValue());
                    detail.setUnitPrice(receivingDetail.getUnitPrice());
                    if (detail.getUnitPrice() != null && detail.getQuantity() != null) {
                        detail.setTotalPrice(detail.getUnitPrice()
                            .multiply(new BigDecimal(detail.getQuantity())));
                        totalAmount = totalAmount.add(detail.getTotalPrice());
                    }
                    detail.setPurchaseDate(receivingDetail.getProductionDate());
                    assetDetails.add(detail);
                }
            }
            
            if (!assetDetails.isEmpty()) {
                assetStorage.setTotalAmount(totalAmount);
                assetStorageService.createStorage(assetStorage, assetDetails);
            }
        } catch (Exception e) {
            // 记录日志但不影响收货确认流程
            System.err.println("创建资产入库单失败：" + e.getMessage());
        }
    }
}

