package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchasePayment;
import com.coal.erp.business.domain.purchase.PurchasePaymentDetail;
import com.coal.erp.business.service.purchase.IPurchasePaymentService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购付款管理控制器
 */
@RestController
@RequestMapping("/api/purchase/payment")
public class PurchasePaymentController {
    
    @Autowired
    private IPurchasePaymentService paymentService;
    
    /**
     * 创建付款单
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:payment:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> paymentMap = (Map<String, Object>) params.get("payment");
            PurchasePayment payment = convertToPayment(paymentMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchasePaymentDetail> details = detailsMap.stream()
                .map(this::convertToPaymentDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return paymentService.createPayment(payment, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 从采购订单创建付款单
     */
    @PostMapping("/from-order/{orderId}")
    @PreAuthorize("hasPermission(null, 'purchase:payment:add')")
    public R<?> createFromOrder(@PathVariable Long orderId, @RequestParam String paymentType) {
        return paymentService.createPaymentFromOrder(orderId, paymentType);
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'purchase:payment:submit')")
    public R<?> submit(@PathVariable Long id) {
        return paymentService.submitPayment(id);
    }
    
    /**
     * 审批通过
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'purchase:payment:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return paymentService.approvePayment(id, approveRemark);
    }
    
    /**
     * 确认付款
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasPermission(null, 'purchase:payment:confirm')")
    public R<?> confirm(@PathVariable Long id) {
        return paymentService.confirmPayment(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:payment:list')")
    public R<Page<PurchasePayment>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String paymentNo,
            @RequestParam(required = false) String status) {
        return R.success(paymentService.pagePayment(current, size, paymentNo, status));
    }
    
    /**
     * 获取付款明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:payment:list')")
    public R<List<PurchasePaymentDetail>> getDetails(@PathVariable Long id) {
        return R.success(paymentService.getPaymentDetails(id));
    }
    
    /**
     * 更新付款单
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:payment:edit')")
    public R<?> update(@RequestBody PurchasePayment payment) {
        return R.success(paymentService.updateById(payment));
    }
    
    /**
     * 删除付款单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:payment:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchasePayment payment = paymentService.getById(id);
        if (payment != null && !"DRAFT".equals(payment.getStatus())) {
            return R.error("只能删除草稿状态的付款单");
        }
        return R.success(paymentService.removeById(id));
    }
    
    // 转换方法
    private PurchasePayment convertToPayment(Map<String, Object> map) {
        PurchasePayment payment = new PurchasePayment();
        if (map.get("orderId") != null) payment.setOrderId(Long.valueOf(map.get("orderId").toString()));
        if (map.get("orderNo") != null) payment.setOrderNo(map.get("orderNo").toString());
        if (map.get("contractId") != null) payment.setContractId(Long.valueOf(map.get("contractId").toString()));
        if (map.get("contractNo") != null) payment.setContractNo(map.get("contractNo").toString());
        if (map.get("supplierId") != null) payment.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        if (map.get("supplierName") != null) payment.setSupplierName(map.get("supplierName").toString());
        if (map.get("paymentType") != null) payment.setPaymentType(map.get("paymentType").toString());
        if (map.get("paymentDate") != null) {
            try {
                payment.setPaymentDate(java.sql.Date.valueOf(map.get("paymentDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("paymentMethod") != null) payment.setPaymentMethod(map.get("paymentMethod").toString());
        if (map.get("currency") != null) payment.setCurrency(map.get("currency").toString());
        if (map.get("bankName") != null) payment.setBankName(map.get("bankName").toString());
        if (map.get("bankAccount") != null) payment.setBankAccount(map.get("bankAccount").toString());
        if (map.get("accountName") != null) payment.setAccountName(map.get("accountName").toString());
        if (map.get("voucherNo") != null) payment.setVoucherNo(map.get("voucherNo").toString());
        if (map.get("remark") != null) payment.setRemark(map.get("remark").toString());
        return payment;
    }
    
    private PurchasePaymentDetail convertToPaymentDetail(Map<String, Object> map) {
        PurchasePaymentDetail detail = new PurchasePaymentDetail();
        if (map.get("orderId") != null) detail.setOrderId(Long.valueOf(map.get("orderId").toString()));
        if (map.get("orderNo") != null) detail.setOrderNo(map.get("orderNo").toString());
        if (map.get("receivingId") != null) detail.setReceivingId(Long.valueOf(map.get("receivingId").toString()));
        if (map.get("receivingNo") != null) detail.setReceivingNo(map.get("receivingNo").toString());
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("paymentAmount") != null) detail.setPaymentAmount(new java.math.BigDecimal(map.get("paymentAmount").toString()));
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

