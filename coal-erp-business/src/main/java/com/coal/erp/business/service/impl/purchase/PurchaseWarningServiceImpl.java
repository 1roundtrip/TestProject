package com.coal.erp.business.service.impl.purchase;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.PurchaseOrder;
import com.coal.erp.business.domain.WarningAlert;
import com.coal.erp.business.domain.purchase.PurchasePayment;
import com.coal.erp.business.domain.purchase.PurchaseQualityCheck;
import com.coal.erp.business.domain.purchase.PurchaseSupplier;
import com.coal.erp.business.mapper.PurchaseOrderMapper;
import com.coal.erp.business.mapper.purchase.PurchasePaymentMapper;
import com.coal.erp.business.mapper.purchase.PurchaseQualityCheckMapper;
import com.coal.erp.business.mapper.purchase.PurchaseSupplierMapper;
import com.coal.erp.business.service.IWarningAlertService;
import com.coal.erp.business.service.purchase.IPurchaseWarningService;
import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 采购预警服务实现
 */
@Service
public class PurchaseWarningServiceImpl implements IPurchaseWarningService {
    
    @Autowired
    private PurchaseOrderMapper orderMapper;
    
    @Autowired
    private PurchasePaymentMapper paymentMapper;
    
    @Autowired
    private PurchaseQualityCheckMapper qualityCheckMapper;
    
    @Autowired
    private PurchaseSupplierMapper supplierMapper;
    
    @Autowired
    private IWarningAlertService warningAlertService;
    
    @Override
    public R<?> checkOrderOverdue() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 3); // 交货日期前3天预警
        Date warningDate = cal.getTime();
        
        // 查询即将到期或已超期的订单
        List<PurchaseOrder> overdueOrders = orderMapper.selectList(
            new LambdaQueryWrapper<PurchaseOrder>()
                .in(PurchaseOrder::getStatus, 
                    java.util.Arrays.asList("CONFIRMED", "EXECUTING", "PARTIAL_RECEIVED"))
                .le(PurchaseOrder::getDeliveryDate, warningDate)
                .isNotNull(PurchaseOrder::getDeliveryDate)
        );
        
        for (PurchaseOrder order : overdueOrders) {
            long daysDiff = (order.getDeliveryDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            String alertLevel = daysDiff < 0 ? "RED" : (daysDiff <= 3 ? "ORANGE" : "YELLOW");
            
            WarningAlert alert = new WarningAlert();
            alert.setAlertType("ORDER_OVERDUE");
            alert.setAlertLevel(alertLevel);
            alert.setAlertTitle("采购订单超期预警");
            alert.setAlertContent(String.format("订单号：%s，供应商：%s，交货日期：%s，已超期%d天",
                order.getOrderNo(), order.getSupplierName(), order.getDeliveryDate(), Math.abs(daysDiff)));
            alert.setExpireDate(order.getDeliveryDate());
            alert.setDaysRemaining((int) daysDiff);
            alert.setCreateUserId(SecurityUtils.getUserId());
            alert.setCreateTime(now);
            
            warningAlertService.createAlert(alert);
        }
        
        return R.success("检查完成，发现" + overdueOrders.size() + "个超期订单");
    }
    
    @Override
    public R<?> checkPaymentOverdue() {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, 7); // 付款到期后7天预警
        Date warningDate = cal.getTime();
        
        // 查询超期未付款的付款单
        List<PurchasePayment> overduePayments = paymentMapper.selectList(
            new LambdaQueryWrapper<PurchasePayment>()
                .eq(PurchasePayment::getStatus, "APPROVED")
                .le(PurchasePayment::getPaymentDate, warningDate)
                .isNotNull(PurchasePayment::getPaymentDate)
        );
        
        for (PurchasePayment payment : overduePayments) {
            long daysDiff = (payment.getPaymentDate().getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            
            WarningAlert alert = new WarningAlert();
            alert.setAlertType("PAYMENT_OVERDUE");
            alert.setAlertLevel("ORANGE");
            alert.setAlertTitle("采购付款超期预警");
            alert.setAlertContent(String.format("付款单号：%s，供应商：%s，付款日期：%s，已超期%d天",
                payment.getPaymentNo(), payment.getSupplierName(), payment.getPaymentDate(), Math.abs(daysDiff)));
            alert.setExpireDate(payment.getPaymentDate());
            alert.setDaysRemaining((int) daysDiff);
            alert.setCreateUserId(SecurityUtils.getUserId());
            alert.setCreateTime(now);
            
            warningAlertService.createAlert(alert);
        }
        
        return R.success("检查完成，发现" + overduePayments.size() + "个超期付款单");
    }
    
    @Override
    public R<?> checkQualityIssue() {
        // 查询质检合格率低于90%的供应商
        List<PurchaseQualityCheck> qualityChecks = qualityCheckMapper.selectList(
            new LambdaQueryWrapper<PurchaseQualityCheck>()
                .eq(PurchaseQualityCheck::getStatus, "COMPLETED")
                .lt(PurchaseQualityCheck::getQualifiedRate, 90.0)
        );
        
        for (PurchaseQualityCheck check : qualityChecks) {
            WarningAlert alert = new WarningAlert();
            alert.setAlertType("QUALITY_ISSUE");
            alert.setAlertLevel("RED");
            alert.setAlertTitle("采购质量问题预警");
            alert.setAlertContent(String.format("质检单号：%s，供应商：%s，合格率：%.2f%%，低于标准",
                check.getCheckNo(), check.getSupplierName(), check.getQualifiedRate()));
            alert.setCreateUserId(SecurityUtils.getUserId());
            alert.setCreateTime(new Date());
            
            warningAlertService.createAlert(alert);
        }
        
        return R.success("检查完成，发现" + qualityChecks.size() + "个质量问题");
    }
    
    @Override
    public R<?> checkSupplierRisk() {
        // 查询综合评分低于6分的供应商
        List<PurchaseSupplier> riskSuppliers = supplierMapper.selectList(
            new LambdaQueryWrapper<PurchaseSupplier>()
                .eq(PurchaseSupplier::getStatus, "ACTIVE")
                .lt(PurchaseSupplier::getTotalRating, 6.0)
        );
        
        for (PurchaseSupplier supplier : riskSuppliers) {
            WarningAlert alert = new WarningAlert();
            alert.setAlertType("SUPPLIER_RISK");
            alert.setAlertLevel("ORANGE");
            alert.setAlertTitle("供应商风险预警");
            alert.setAlertContent(String.format("供应商：%s，综合评分：%.1f，低于标准，建议关注",
                supplier.getSupplierName(), supplier.getTotalRating()));
            alert.setCreateUserId(SecurityUtils.getUserId());
            alert.setCreateTime(new Date());
            
            warningAlertService.createAlert(alert);
        }
        
        return R.success("检查完成，发现" + riskSuppliers.size() + "个风险供应商");
    }
}

