package com.coal.erp.business.controller.purchase;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.purchase.PurchaseContract;
import com.coal.erp.business.domain.purchase.PurchaseContractDetail;
import com.coal.erp.business.service.purchase.IPurchaseContractService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 采购合同管理控制器
 */
@RestController
@RequestMapping("/api/purchase/contract")
public class PurchaseContractController {
    
    @Autowired
    private IPurchaseContractService contractService;
    
    /**
     * 创建采购合同
     */
    @PostMapping
    @PreAuthorize("hasPermission(null, 'purchase:contract:add')")
    public R<?> create(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> contractMap = (Map<String, Object>) params.get("contract");
            PurchaseContract contract = convertToContract(contractMap);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> detailsMap = (List<Map<String, Object>>) params.get("details");
            List<PurchaseContractDetail> details = detailsMap.stream()
                .map(this::convertToContractDetail)
                .collect(java.util.stream.Collectors.toList());
            
            return contractService.createContract(contract, details);
        } catch (Exception e) {
            return R.error("创建失败：" + e.getMessage());
        }
    }
    
    /**
     * 从采购订单创建合同
     */
    @PostMapping("/from-order/{orderId}")
    @PreAuthorize("hasPermission(null, 'purchase:contract:add')")
    public R<?> createFromOrder(@PathVariable Long orderId) {
        return contractService.createContractFromOrder(orderId);
    }
    
    /**
     * 提交审批
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasPermission(null, 'purchase:contract:submit')")
    public R<?> submit(@PathVariable Long id) {
        return contractService.submitContract(id);
    }
    
    /**
     * 审批通过
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasPermission(null, 'purchase:contract:approve')")
    public R<?> approve(@PathVariable Long id, @RequestParam(required = false) String approveRemark) {
        return contractService.approveContract(id, approveRemark);
    }
    
    /**
     * 签订合同
     */
    @PostMapping("/{id}/sign")
    @PreAuthorize("hasPermission(null, 'purchase:contract:sign')")
    public R<?> sign(@PathVariable Long id) {
        return contractService.signContract(id);
    }
    
    /**
     * 分页查询
     */
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'purchase:contract:list')")
    public R<Page<PurchaseContract>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String status) {
        return R.success(contractService.pageContract(current, size, contractNo, status));
    }
    
    /**
     * 获取合同明细
     */
    @GetMapping("/{id}/details")
    @PreAuthorize("hasPermission(null, 'purchase:contract:list')")
    public R<List<PurchaseContractDetail>> getDetails(@PathVariable Long id) {
        return R.success(contractService.getContractDetails(id));
    }
    
    /**
     * 更新合同
     */
    @PutMapping
    @PreAuthorize("hasPermission(null, 'purchase:contract:edit')")
    public R<?> update(@RequestBody PurchaseContract contract) {
        return R.success(contractService.updateById(contract));
    }
    
    /**
     * 删除合同
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'purchase:contract:remove')")
    public R<?> delete(@PathVariable Long id) {
        PurchaseContract contract = contractService.getById(id);
        if (contract != null && !"DRAFT".equals(contract.getStatus())) {
            return R.error("只能删除草稿状态的合同");
        }
        return R.success(contractService.removeById(id));
    }
    
    // 转换方法
    private PurchaseContract convertToContract(Map<String, Object> map) {
        PurchaseContract contract = new PurchaseContract();
        if (map.get("contractName") != null) contract.setContractName(map.get("contractName").toString());
        if (map.get("orderId") != null) contract.setOrderId(Long.valueOf(map.get("orderId").toString()));
        if (map.get("orderNo") != null) contract.setOrderNo(map.get("orderNo").toString());
        if (map.get("supplierId") != null) contract.setSupplierId(Long.valueOf(map.get("supplierId").toString()));
        if (map.get("supplierName") != null) contract.setSupplierName(map.get("supplierName").toString());
        if (map.get("contractType") != null) contract.setContractType(map.get("contractType").toString());
        if (map.get("contractDate") != null) {
            try {
                contract.setContractDate(java.sql.Date.valueOf(map.get("contractDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("startDate") != null) {
            try {
                contract.setStartDate(java.sql.Date.valueOf(map.get("startDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("endDate") != null) {
            try {
                contract.setEndDate(java.sql.Date.valueOf(map.get("endDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("currency") != null) contract.setCurrency(map.get("currency").toString());
        if (map.get("paymentMethod") != null) contract.setPaymentMethod(map.get("paymentMethod").toString());
        if (map.get("paymentSchedule") != null) contract.setPaymentSchedule(map.get("paymentSchedule").toString());
        if (map.get("deliveryTerms") != null) contract.setDeliveryTerms(map.get("deliveryTerms").toString());
        if (map.get("qualityTerms") != null) contract.setQualityTerms(map.get("qualityTerms").toString());
        if (map.get("warrantyTerms") != null) contract.setWarrantyTerms(map.get("warrantyTerms").toString());
        if (map.get("penaltyTerms") != null) contract.setPenaltyTerms(map.get("penaltyTerms").toString());
        if (map.get("contractFile") != null) contract.setContractFile(map.get("contractFile").toString());
        if (map.get("remark") != null) contract.setRemark(map.get("remark").toString());
        return contract;
    }
    
    private PurchaseContractDetail convertToContractDetail(Map<String, Object> map) {
        PurchaseContractDetail detail = new PurchaseContractDetail();
        if (map.get("itemName") != null) detail.setItemName(map.get("itemName").toString());
        if (map.get("itemCode") != null) detail.setItemCode(map.get("itemCode").toString());
        if (map.get("specification") != null) detail.setSpecification(map.get("specification").toString());
        if (map.get("unit") != null) detail.setUnit(map.get("unit").toString());
        if (map.get("quantity") != null) detail.setQuantity(new java.math.BigDecimal(map.get("quantity").toString()));
        if (map.get("unitPrice") != null) detail.setUnitPrice(new java.math.BigDecimal(map.get("unitPrice").toString()));
        if (map.get("deliveryDate") != null) {
            try {
                detail.setDeliveryDate(java.sql.Date.valueOf(map.get("deliveryDate").toString()));
            } catch (Exception e) {
                // 忽略日期解析错误
            }
        }
        if (map.get("remark") != null) detail.setRemark(map.get("remark").toString());
        return detail;
    }
}

