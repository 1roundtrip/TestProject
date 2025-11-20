package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.purchase.PurchaseSupplier;
import com.coal.erp.business.domain.purchase.PurchaseSupplierEvaluation;
import com.coal.erp.business.mapper.purchase.PurchaseSupplierEvaluationMapper;
import com.coal.erp.business.mapper.purchase.PurchaseSupplierMapper;
import com.coal.erp.business.service.purchase.IPurchaseSupplierService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 供应商服务实现
 */
@Service
public class PurchaseSupplierServiceImpl extends ServiceImpl<PurchaseSupplierMapper, PurchaseSupplier> 
        implements IPurchaseSupplierService {
    
    @Autowired
    private PurchaseSupplierEvaluationMapper evaluationMapper;
    
    @Override
    public Page<PurchaseSupplier> pageSupplier(Long current, Long size, String supplierName, String status) {
        Page<PurchaseSupplier> page = new Page<>(current, size);
        LambdaQueryWrapper<PurchaseSupplier> wrapper = new LambdaQueryWrapper<>();
        if (supplierName != null && !supplierName.isEmpty()) {
            wrapper.like(PurchaseSupplier::getSupplierName, supplierName);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq(PurchaseSupplier::getStatus, status);
        }
        wrapper.orderByDesc(PurchaseSupplier::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> evaluateSupplier(PurchaseSupplierEvaluation evaluation) {
        evaluation.setEvaluationDate(new Date());
        evaluation.setEvaluatorId(SecurityUtils.getUserId());
        evaluation.setEvaluatorName(SecurityUtils.getUsername());
        evaluation.setCreateTime(new Date());
        
        // 计算综合评分
        BigDecimal totalScore = BigDecimal.ZERO;
        int count = 0;
        if (evaluation.getQualityScore() != null) {
            totalScore = totalScore.add(evaluation.getQualityScore());
            count++;
        }
        if (evaluation.getDeliveryScore() != null) {
            totalScore = totalScore.add(evaluation.getDeliveryScore());
            count++;
        }
        if (evaluation.getServiceScore() != null) {
            totalScore = totalScore.add(evaluation.getServiceScore());
            count++;
        }
        if (evaluation.getPriceScore() != null) {
            totalScore = totalScore.add(evaluation.getPriceScore());
            count++;
        }
        if (count > 0) {
            evaluation.setTotalScore(totalScore.divide(new BigDecimal(count), 1, BigDecimal.ROUND_HALF_UP));
        }
        
        evaluationMapper.insert(evaluation);
        
        // 更新供应商评分
        updateSupplierRating(evaluation.getSupplierId());
        
        return R.success(evaluation);
    }
    
    @Override
    public List<PurchaseSupplierEvaluation> getSupplierEvaluations(Long supplierId) {
        LambdaQueryWrapper<PurchaseSupplierEvaluation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PurchaseSupplierEvaluation::getSupplierId, supplierId);
        wrapper.orderByDesc(PurchaseSupplierEvaluation::getEvaluationDate);
        return evaluationMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateSupplierRating(Long supplierId) {
        PurchaseSupplier supplier = getById(supplierId);
        if (supplier == null) {
            return R.error("供应商不存在");
        }
        
        // 查询最近评价记录，计算平均分
        List<PurchaseSupplierEvaluation> evaluations = getSupplierEvaluations(supplierId);
        if (evaluations.isEmpty()) {
            return R.success();
        }
        
        BigDecimal totalQuality = BigDecimal.ZERO;
        BigDecimal totalService = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalScore = BigDecimal.ZERO;
        int count = 0;
        
        for (PurchaseSupplierEvaluation eval : evaluations) {
            if (eval.getQualityScore() != null) {
                totalQuality = totalQuality.add(eval.getQualityScore());
            }
            if (eval.getServiceScore() != null) {
                totalService = totalService.add(eval.getServiceScore());
            }
            if (eval.getPriceScore() != null) {
                totalPrice = totalPrice.add(eval.getPriceScore());
            }
            if (eval.getTotalScore() != null) {
                totalScore = totalScore.add(eval.getTotalScore());
                count++;
            }
        }
        
        if (count > 0) {
            supplier.setQualityRating(totalQuality.divide(new BigDecimal(count), 1, BigDecimal.ROUND_HALF_UP));
            supplier.setServiceRating(totalService.divide(new BigDecimal(count), 1, BigDecimal.ROUND_HALF_UP));
            supplier.setPriceRating(totalPrice.divide(new BigDecimal(count), 1, BigDecimal.ROUND_HALF_UP));
            supplier.setTotalRating(totalScore.divide(new BigDecimal(count), 1, BigDecimal.ROUND_HALF_UP));
            updateById(supplier);
        }
        
        return R.success();
    }
}

