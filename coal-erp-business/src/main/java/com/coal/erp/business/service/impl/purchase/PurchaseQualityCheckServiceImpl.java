package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.asset.AssetStorage;
import com.coal.erp.business.domain.asset.AssetStorageDetail;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheck;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheckDetail;
import com.coal.erp.business.domain.purchase.PurchaseReceiving;
import com.coal.erp.business.domain.purchase.PurchaseReceivingDetail;
import com.coal.erp.business.mapper.purchase.PurchaseQualityCheckDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseQualityCheckMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReceivingDetailMapper;
import com.coal.erp.business.mapper.purchase.PurchaseReceivingMapper;
import com.coal.erp.business.service.asset.IAssetStorageService;
import com.coal.erp.business.service.purchase.IPurchaseQualityCheckService;
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
 * 采购质检服务实现
 */
@Service
public class PurchaseQualityCheckServiceImpl extends ServiceImpl<PurchaseQualityCheckMapper, PurchaseQualityCheck> 
        implements IPurchaseQualityCheckService {
    
    @Autowired
    private PurchaseQualityCheckDetailMapper checkDetailMapper;
    
    @Autowired
    private PurchaseReceivingMapper receivingMapper;
    
    @Autowired
    private PurchaseReceivingDetailMapper receivingDetailMapper;
    
    @Autowired
    private IAssetStorageService assetStorageService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createQualityCheck(PurchaseQualityCheck qualityCheck, List<PurchaseQualityCheckDetail> details) {
        // 生成质检单号
        qualityCheck.setCheckNo("ZJ" + System.currentTimeMillis());
        qualityCheck.setStatus("DRAFT");
        qualityCheck.setCheckDate(new Date());
        qualityCheck.setCreateTime(new Date());
        
        // 计算总数量和合格率
        BigDecimal totalQuantity = BigDecimal.ZERO;
        BigDecimal qualifiedQuantity = BigDecimal.ZERO;
        
        for (PurchaseQualityCheckDetail detail : details) {
            if (detail.getCheckQuantity() != null) {
                totalQuantity = totalQuantity.add(detail.getCheckQuantity());
            }
            if (detail.getQualifiedQuantity() != null) {
                qualifiedQuantity = qualifiedQuantity.add(detail.getQualifiedQuantity());
            }
        }
        
        qualityCheck.setTotalQuantity(totalQuantity);
        qualityCheck.setQualifiedQuantity(qualifiedQuantity);
        qualityCheck.setUnqualifiedQuantity(totalQuantity.subtract(qualifiedQuantity));
        
        if (totalQuantity.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rate = qualifiedQuantity
                .divide(totalQuantity, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));
            qualityCheck.setQualifiedRate(rate);
        }
        
        // 判断检验结果
        if (qualifiedQuantity.compareTo(totalQuantity) == 0) {
            qualityCheck.setCheckResult("PASSED");
        } else if (qualifiedQuantity.compareTo(BigDecimal.ZERO) == 0) {
            qualityCheck.setCheckResult("FAILED");
        } else {
            qualityCheck.setCheckResult("PARTIAL");
        }
        
        // 保存质检单
        save(qualityCheck);
        
        // 保存明细
        details.forEach(detail -> {
            detail.setCheckId(qualityCheck.getCheckId());
            checkDetailMapper.insert(detail);
        });
        
        return R.success(qualityCheck);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> completeQualityCheck(Long checkId) {
        PurchaseQualityCheck qualityCheck = getById(checkId);
        if (qualityCheck == null) {
            return R.error("质检单不存在");
        }
        qualityCheck.setStatus("COMPLETED");
        qualityCheck.setCheckerId(SecurityUtils.getUserId());
        qualityCheck.setCheckerName(SecurityUtils.getUsername());
        qualityCheck.setUpdateTime(new Date());
        updateById(qualityCheck);
        
        // 更新收货单质检状态
        if (qualityCheck.getReceivingId() != null) {
            PurchaseReceiving receiving = receivingMapper.selectById(qualityCheck.getReceivingId());
            if (receiving != null) {
                if ("PASSED".equals(qualityCheck.getCheckResult())) {
                    receiving.setStatus("QUALITY_PASSED");
                } else {
                    receiving.setStatus("QUALITY_FAILED");
                }
                receiving.setUpdateTime(new Date());
                receivingMapper.updateById(receiving);
                
                // 更新收货明细质检状态
                List<PurchaseReceivingDetail> receivingDetails = receivingDetailMapper.selectList(
                    new LambdaQueryWrapper<PurchaseReceivingDetail>()
                        .eq(PurchaseReceivingDetail::getReceivingId, qualityCheck.getReceivingId())
                );
                
                List<PurchaseQualityCheckDetail> checkDetails = getQualityCheckDetails(checkId);
                
                // 如果质检通过，自动创建资产入库单（与资产入库流程集成）
                if ("QUALITY_PASSED".equals(qualityCheck.getCheckResult())) {
                    receiving.setStatus("STORED");
                    receivingMapper.updateById(receiving);
                    createAssetStorageFromReceiving(receiving, checkDetails);
                }
                for (PurchaseQualityCheckDetail checkDetail : checkDetails) {
                    if (checkDetail.getReceivingDetailId() != null) {
                        receivingDetails.stream()
                            .filter(rd -> rd.getDetailId().equals(checkDetail.getReceivingDetailId()))
                            .forEach(rd -> {
                                rd.setQualityStatus(checkDetail.getCheckResult());
                                rd.setQualifiedQuantity(checkDetail.getQualifiedQuantity());
                                rd.setUnqualifiedQuantity(checkDetail.getUnqualifiedQuantity());
                                receivingDetailMapper.updateById(rd);
                            });
                    }
                }
            }
        }
        
        return R.success();
    }
    
    @Override
    public Page<PurchaseQualityCheck> pageQualityCheck(Long current, Long size, String checkNo, String status) {
        Page<PurchaseQualityCheck> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseQualityCheck> wrapper = new LambdaQueryWrapper<>();
        if (checkNo != null && !checkNo.isEmpty()) {
            wrapper.like(PurchaseQualityCheck::getCheckNo, checkNo);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseQualityCheck::getStatus, status);
        }
        wrapper.orderByDesc(PurchaseQualityCheck::getCheckDate);
        return page(page, wrapper);
    }
    
    @Override
    public List<PurchaseQualityCheckDetail> getQualityCheckDetails(Long checkId) {
        LambdaQueryWrapper<PurchaseQualityCheckDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseQualityCheckDetail::getCheckId, checkId);
        return checkDetailMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createQualityCheckFromReceiving(Long receivingId) {
        PurchaseReceiving receiving = receivingMapper.selectById(receivingId);
        if (receiving == null) {
            return R.error("收货单不存在");
        }
        if (!"CONFIRMED".equals(receiving.getStatus())) {
            return R.error("只能对已确认的收货单进行质检");
        }
        
        // 查询收货明细
        LambdaQueryWrapper<PurchaseReceivingDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(PurchaseReceivingDetail::getReceivingId, receivingId);
        List<PurchaseReceivingDetail> receivingDetails = receivingDetailMapper.selectList(detailWrapper);
        
        if (receivingDetails.isEmpty()) {
            return R.error("收货单没有明细");
        }
        
        // 创建质检单
        PurchaseQualityCheck qualityCheck = new PurchaseQualityCheck();
        qualityCheck.setReceivingId(receivingId);
        qualityCheck.setReceivingNo(receiving.getReceivingNo());
        qualityCheck.setOrderId(receiving.getOrderId());
        qualityCheck.setOrderNo(receiving.getOrderNo());
        qualityCheck.setSupplierId(receiving.getSupplierId());
        qualityCheck.setSupplierName(receiving.getSupplierName());
        qualityCheck.setCheckDate(new Date());
        qualityCheck.setCheckType("INCOMING");
        qualityCheck.setStatus("DRAFT");
        qualityCheck.setCreateTime(new Date());
        
        // 创建质检明细
        List<PurchaseQualityCheckDetail> checkDetails = receivingDetails.stream()
            .map(receivingDetail -> {
                PurchaseQualityCheckDetail detail = new PurchaseQualityCheckDetail();
                detail.setReceivingDetailId(receivingDetail.getDetailId());
                detail.setItemName(receivingDetail.getItemName());
                detail.setItemCode(receivingDetail.getItemCode());
                detail.setSpecification(receivingDetail.getSpecification());
                detail.setCheckQuantity(receivingDetail.getReceivedQuantity());
                detail.setQualifiedQuantity(BigDecimal.ZERO);
                detail.setUnqualifiedQuantity(BigDecimal.ZERO);
                detail.setCheckResult("PENDING");
                return detail;
            })
            .collect(Collectors.toList());
        
        return createQualityCheck(qualityCheck, checkDetails);
    }
    
    /**
     * 从采购收货创建资产入库单（与资产入库流程集成）
     */
    private void createAssetStorageFromReceiving(PurchaseReceiving receiving, List<PurchaseQualityCheckDetail> checkDetails) {
        try {
            // 查询收货明细
            List<PurchaseReceivingDetail> receivingDetails = receivingDetailMapper.selectList(
                new LambdaQueryWrapper<PurchaseReceivingDetail>()
                    .eq(PurchaseReceivingDetail::getReceivingId, receiving.getReceivingId())
            );
            
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
            
            // 创建资产入库明细（只包含质检合格的物料）
            List<AssetStorageDetail> assetDetails = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (PurchaseReceivingDetail receivingDetail : receivingDetails) {
                // 查找对应的质检明细
                PurchaseQualityCheckDetail checkDetail = checkDetails.stream()
                    .filter(cd -> cd.getReceivingDetailId() != null && 
                                 cd.getReceivingDetailId().equals(receivingDetail.getDetailId()) &&
                                 "PASSED".equals(cd.getCheckResult()))
                    .findFirst()
                    .orElse(null);
                
                if (checkDetail != null && checkDetail.getQualifiedQuantity() != null &&
                    checkDetail.getQualifiedQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    
                    AssetStorageDetail detail = new AssetStorageDetail();
                    detail.setAssetName(receivingDetail.getItemName());
                    detail.setAssetCode(receivingDetail.getItemCode());
                    detail.setModel(receivingDetail.getSpecification());
                    detail.setQuantity(checkDetail.getQualifiedQuantity().intValue());
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
            // 记录日志但不影响质检完成流程
            System.err.println("创建资产入库单失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}

