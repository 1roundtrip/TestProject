package com.coal.erp.business.service.integration.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.asset.AssetStorage;
import com.coal.erp.business.domain.asset.AssetStorageDetail;
import com.coal.erp.business.domain.finance.FinancePayment;
import com.coal.erp.business.domain.inventory.InventoryMaterial;
import com.coal.erp.business.domain.inventory.InventoryWarning;
import com.coal.erp.business.domain.maintenance.MaintenanceWorkOrder;
import com.coal.erp.business.domain.purchase.PurchaseReceiving;
import com.coal.erp.business.domain.purchase.PurchaseReceivingDetail;
import com.coal.erp.business.domain.purchase.PurchaseRequisition;
import com.coal.erp.business.domain.purchase.PurchaseRequisitionDetail;
import com.coal.erp.business.domain.warning.WarningRecord;
import com.coal.erp.business.domain.warning.WarningNotification;
import com.coal.erp.business.domain.warning.WarningHandleRecord;
import com.coal.erp.business.event.*;
import com.coal.erp.business.mapper.finance.FinancePaymentMapper;
import com.coal.erp.business.mapper.inventory.InventoryMaterialMapper;
import com.coal.erp.business.mapper.inventory.InventoryWarningMapper;
import com.coal.erp.business.mapper.maintenance.MaintenanceWorkOrderMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReceivingDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReceivingMapper;
import com.coal.erp.business.mapper.purchase.PurchaseRequisitionDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseRequisitionMapper;
import com.coal.erp.business.mapper.warning.WarningHandleRecordMapper;
import com.coal.erp.business.mapper.warning.WarningNotificationMapper;
import com.coal.erp.business.mapper.warning.WarningRecordMapper;
import com.coal.erp.business.service.asset.IAssetStorageService;
import com.coal.erp.business.service.integration.IBusinessIntegrationService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 业务流程集成服务实现
 */
@Service
public class BusinessIntegrationServiceImpl implements IBusinessIntegrationService {
    
    private static final Logger log = LoggerFactory.getLogger(BusinessIntegrationServiceImpl.class);
    
    @Autowired
    private PurchaseReceivingMapper purchaseReceivingMapper;
    
    @Autowired
    private PurchaseReceivingDetailMapper purchaseReceivingDetailMapper;
    
    @Autowired
    private IAssetStorageService assetStorageService;
    
    @Autowired
    private FinancePaymentMapper financePaymentMapper;
    
    @Autowired
    private WarningRecordMapper warningRecordMapper;
    
    @Autowired
    private MaintenanceWorkOrderMapper maintenanceWorkOrderMapper;
    
    @Autowired
    private InventoryWarningMapper inventoryWarningMapper;
    
    @Autowired
    private PurchaseRequisitionMapper purchaseRequisitionMapper;
    
    @Autowired
    private PurchaseRequisitionDetailMapper purchaseRequisitionDetailMapper;
    
    @Autowired
    private InventoryMaterialMapper inventoryMaterialMapper;
    
    @Autowired
    private WarningNotificationMapper warningNotificationMapper;
    
    @Autowired
    private WarningHandleRecordMapper warningHandleRecordMapper;
    
    @Autowired
    private BusinessEventPublisher eventPublisher;
    
    /**
     * 采购到资产流程集成
     * 采购订单 → 收货入库 → 资产建档 → 财务付款
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> integratePurchaseToAsset(Long receivingId) {
        try {
            log.info("开始采购到资产流程集成: receivingId={}", receivingId);
            
            // 1. 查询收货单
            PurchaseReceiving receiving = purchaseReceivingMapper.selectById(receivingId);
            if (receiving == null) {
                return R.error("收货单不存在");
            }
            
            if (!"CONFIRMED".equals(receiving.getStatus())) {
                return R.error("只能处理已确认的收货单");
            }
            
            // 2. 查询收货明细
            List<PurchaseReceivingDetail> receivingDetails = purchaseReceivingDetailMapper.selectList(
                new LambdaQueryWrapper<PurchaseReceivingDetail>()
                    .eq(PurchaseReceivingDetail::getReceivingId, receivingId)
            );
            
            if (receivingDetails.isEmpty()) {
                return R.error("收货明细为空");
            }
            
            // 3. 创建资产入库单
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
            
            List<AssetStorageDetail> assetDetails = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (PurchaseReceivingDetail detail : receivingDetails) {
                if ("PASSED".equals(detail.getQualityStatus()) && 
                    detail.getQualifiedQuantity() != null &&
                    detail.getQualifiedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    
                    AssetStorageDetail assetDetail = new AssetStorageDetail();
                    assetDetail.setAssetName(detail.getItemName());
                    assetDetail.setAssetCode(detail.getItemCode());
                    assetDetail.setModel(detail.getSpecification());
                    assetDetail.setQuantity(detail.getQualifiedQuantity().intValue());
                    assetDetail.setUnitPrice(detail.getUnitPrice());
                    if (assetDetail.getUnitPrice() != null && assetDetail.getQuantity() != null) {
                        assetDetail.setTotalPrice(assetDetail.getUnitPrice()
                            .multiply(new BigDecimal(assetDetail.getQuantity())));
                        totalAmount = totalAmount.add(assetDetail.getTotalPrice());
                    }
                    assetDetail.setPurchaseDate(detail.getProductionDate());
                    assetDetails.add(assetDetail);
                }
            }
            
            if (!assetDetails.isEmpty()) {
                assetStorage.setTotalAmount(totalAmount);
                R<?> storageResult = assetStorageService.createStorage(assetStorage, assetDetails);
                if (!storageResult.isSuccess()) {
                    return R.error("创建资产入库单失败: " + storageResult.getMsg());
                }
                
                // 4. 创建财务付款单
                FinancePayment payment = new FinancePayment();
                payment.setPaymentNo("PAY" + System.currentTimeMillis());
                payment.setPaymentType("PURCHASE");
                payment.setPaymentDate(new Date());
                payment.setSupplierId(receiving.getSupplierId());
                payment.setAmount(totalAmount);
                payment.setStatus("PENDING");
                payment.setCreateUserId(SecurityUtils.getUserId());
                payment.setCreateTime(new Date());
                
                financePaymentMapper.insert(payment);
                
                // 5. 发布事件
                PurchaseToAssetEvent event = new PurchaseToAssetEvent();
                event.setOrderId(receiving.getOrderId());
                event.setOrderNo(receiving.getOrderNo());
                event.setReceivingId(receivingId);
                event.setReceivingNo(receiving.getReceivingNo());
                event.setSupplierId(receiving.getSupplierId());
                event.setSupplierName(receiving.getSupplierName());
                event.setAssetStorageId(assetStorage.getStorageId());
                event.setAssetStorageNo(assetStorage.getStorageNo());
                event.setPaymentId(payment.getPaymentId());
                event.setPaymentNo(payment.getPaymentNo());
                event.setStep("ASSET_STORAGE_COMPLETED");
                event.setBusinessId(receivingId);
                event.setBusinessNo(receiving.getReceivingNo());
                
                eventPublisher.publishPurchaseToAssetEvent(event);
                
                log.info("采购到资产流程集成完成: receivingId={}, assetStorageId={}, paymentId={}", 
                    receivingId, assetStorage.getStorageId(), payment.getPaymentId());
                
                return R.success();
            } else {
                return R.error("没有合格的物料可以入库");
            }
        } catch (Exception e) {
            log.error("采购到资产流程集成失败", e);
            return R.error("集成失败: " + e.getMessage());
        }
    }
    
    /**
     * 维修业务流集成
     * 设备预警 → 维修工单 → 备件领用 → 维修执行 → 费用核算
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> integrateMaintenanceBusiness(Long warningRecordId, Long workOrderId) {
        try {
            log.info("开始维修业务流集成: warningRecordId={}, workOrderId={}", warningRecordId, workOrderId);
            
            // 1. 查询预警记录
            WarningRecord warningRecord = null;
            if (warningRecordId != null) {
                warningRecord = warningRecordMapper.selectById(warningRecordId);
                if (warningRecord == null) {
                    return R.error("预警记录不存在");
                }
            }
            
            // 2. 查询维修工单
            MaintenanceWorkOrder workOrder = maintenanceWorkOrderMapper.selectById(workOrderId);
            if (workOrder == null) {
                return R.error("维修工单不存在");
            }
            
            // 3. 如果工单需要备件，自动创建备件领用单
            if ("PENDING_PARTS".equals(workOrder.getStatus()) || "IN_PROGRESS".equals(workOrder.getStatus())) {
                // 这里可以根据工单的设备信息，自动创建备件领用单
                // 实际实现需要根据业务需求
            }
            
            // 4. 发布事件
            MaintenanceBusinessEvent event = new MaintenanceBusinessEvent();
            event.setWarningRecordId(warningRecordId);
            event.setWorkOrderId(workOrderId);
            event.setWorkOrderNo(workOrder.getWorkOrderNo());
            event.setStep("WORK_ORDER_CREATED");
            event.setBusinessId(workOrderId);
            event.setBusinessNo(workOrder.getWorkOrderNo());
            
            eventPublisher.publishMaintenanceBusinessEvent(event);
            
            log.info("维修业务流集成完成: workOrderId={}", workOrderId);
            
            return R.success();
        } catch (Exception e) {
            log.error("维修业务流集成失败", e);
            return R.error("集成失败: " + e.getMessage());
        }
    }
    
    /**
     * 库存管理流集成
     * 安全库存预警 → 采购申请 → 库存补充 → 资产领用
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> integrateInventoryManagement(Long warningId) {
        try {
            log.info("开始库存管理流集成: warningId={}", warningId);
            
            // 1. 查询库存预警
            InventoryWarning warning = inventoryWarningMapper.selectById(warningId);
            if (warning == null) {
                return R.error("库存预警不存在");
            }
            
            if (!"LOW_STOCK".equals(warning.getWarningType())) {
                return R.error("只能处理低库存预警");
            }
            
            // 2. 创建采购申请
            PurchaseRequisition requisition = new PurchaseRequisition();
            requisition.setRequisitionNo("PR" + System.currentTimeMillis());
            requisition.setRequisitionName("库存补充申请");
            requisition.setStatus("DRAFT");
            requisition.setCreateTime(new Date());
            
            purchaseRequisitionMapper.insert(requisition);
            
            // 查询物料信息并创建明细
            InventoryMaterial material = inventoryMaterialMapper.selectById(warning.getMaterialId());
            if (material != null) {
                PurchaseRequisitionDetail detail = new PurchaseRequisitionDetail();
                detail.setRequisitionId(requisition.getRequisitionId());
                detail.setItemCode(material.getMaterialCode());
                detail.setItemName(material.getMaterialName());
                detail.setSpecification(material.getSpecification());
                detail.setUnit(material.getUnit());
                detail.setPurpose("库存补充");
                // 计算需要采购的数量（安全库存 - 当前库存）
                BigDecimal needQuantity = warning.getSafetyStock().subtract(warning.getCurrentQuantity());
                if (needQuantity.compareTo(BigDecimal.ZERO) > 0) {
                    detail.setQuantity(needQuantity);
                    detail.setEstimatedPrice(material.getUnitPrice());
                    if (detail.getEstimatedPrice() != null) {
                        detail.setEstimatedAmount(detail.getEstimatedPrice().multiply(needQuantity));
                    }
                    purchaseRequisitionDetailMapper.insert(detail);
                }
            }
            
            // 3. 更新预警状态
            warning.setStatus("PROCESSING");
            warning.setHandlerId(SecurityUtils.getUserId());
            warning.setHandlerName(SecurityUtils.getUsername());
            warning.setHandleTime(new Date());
            inventoryWarningMapper.updateById(warning);
            
            // 4. 发布事件
            InventoryBusinessEvent event = new InventoryBusinessEvent();
            event.setWarningId(warningId);
            event.setWarningType(warning.getWarningType());
            event.setPurchaseRequisitionId(requisition.getRequisitionId());
            event.setPurchaseRequisitionNo(requisition.getRequisitionNo());
            event.setWarehouseId(warning.getWarehouseId());
            event.setWarehouseName(warning.getWarehouseName());
            event.setStep("PURCHASE_REQUISITION_CREATED");
            event.setBusinessId(warningId);
            event.setBusinessNo(warning.getWarningNo());
            
            eventPublisher.publishInventoryBusinessEvent(event);
            
            log.info("库存管理流集成完成: warningId={}, requisitionId={}", warningId, requisition.getRequisitionId());
            
            return R.success();
        } catch (Exception e) {
            log.error("库存管理流集成失败", e);
            return R.error("集成失败: " + e.getMessage());
        }
    }
    
    /**
     * 预警处理流集成
     * 规则监控 → 预警触发 → 通知分发 → 处理跟踪 → 结果反馈
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> integrateWarningProcess(Long recordId) {
        try {
            log.info("开始预警处理流集成: recordId={}", recordId);
            
            // 1. 查询预警记录
            WarningRecord record = warningRecordMapper.selectById(recordId);
            if (record == null) {
                return R.error("预警记录不存在");
            }
            
            // 2. 创建通知
            WarningNotification notification = new WarningNotification();
            notification.setRecordId(recordId);
            notification.setChannelType("IN_APP");
            notification.setNotificationTitle(record.getWarningTitle());
            notification.setNotificationContent(record.getWarningContent());
            notification.setSendStatus("PENDING");
            notification.setRetryCount(0);
            notification.setCreateTime(new Date());
            
            warningNotificationMapper.insert(notification);
            
            // 3. 创建处理跟踪记录
            WarningHandleRecord handleRecord = new WarningHandleRecord();
            handleRecord.setRecordId(recordId);
            handleRecord.setHandleType("ASSIGN");
            handleRecord.setHandlerId(SecurityUtils.getUserId());
            handleRecord.setHandlerName(SecurityUtils.getUsername());
            handleRecord.setHandleTime(new Date());
            handleRecord.setHandleAction("自动分配");
            handleRecord.setHandleContent("系统自动分配处理人");
            
            warningHandleRecordMapper.insert(handleRecord);
            
            // 4. 更新预警记录状态
            record.setStatus("PROCESSING");
            record.setHandlerId(SecurityUtils.getUserId());
            record.setHandlerName(SecurityUtils.getUsername());
            record.setHandleTime(new Date());
            warningRecordMapper.updateById(record);
            
            // 5. 发布事件
            WarningProcessEvent event = new WarningProcessEvent();
            event.setRuleId(record.getRuleId());
            event.setRuleCode(record.getRuleCode());
            event.setRecordId(recordId);
            event.setWarningLevel(record.getWarningLevelCode());
            event.setWarningType(record.getWarningType());
            event.setWarningTitle(record.getWarningTitle());
            event.setWarningContent(record.getWarningContent());
            event.setNotificationId(notification.getNotificationId());
            event.setHandleRecordId(handleRecord.getHandleId());
            event.setHandleStatus("PROCESSING");
            event.setStatus("NOTIFICATION_DISTRIBUTED");
            event.setBusinessId(recordId);
            event.setBusinessNo(record.getRuleCode());
            
            eventPublisher.publishWarningProcessEvent(event);
            
            log.info("预警处理流集成完成: recordId={}, notificationId={}, handleRecordId={}", 
                recordId, notification.getNotificationId(), handleRecord.getHandleId());
            
            return R.success();
        } catch (Exception e) {
            log.error("预警处理流集成失败", e);
            return R.error("集成失败: " + e.getMessage());
        }
    }
    
    @Override
    public R<?> syncBusinessStatus(String businessType, Long businessId, String status) {
        // 实现状态同步逻辑
        return R.success();
    }
    
    @Override
    public R<?> rollbackBusiness(String businessType, Long businessId, String reason) {
        // 实现业务回滚逻辑
        return R.success();
    }
}

