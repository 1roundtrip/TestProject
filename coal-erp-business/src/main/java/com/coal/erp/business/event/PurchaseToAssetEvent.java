package com.coal.erp.business.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 采购到资产流程事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseToAssetEvent extends BusinessEvent {
    
    /**
     * 采购订单ID
     */
    private Long orderId;
    
    /**
     * 采购订单号
     */
    private String orderNo;
    
    /**
     * 收货单ID
     */
    private Long receivingId;
    
    /**
     * 收货单号
     */
    private String receivingNo;
    
    /**
     * 供应商ID
     */
    private Long supplierId;
    
    /**
     * 供应商名称
     */
    private String supplierName;
    
    /**
     * 资产入库单ID
     */
    private Long assetStorageId;
    
    /**
     * 资产入库单号
     */
    private String assetStorageNo;
    
    /**
     * 财务付款单ID
     */
    private Long paymentId;
    
    /**
     * 财务付款单号
     */
    private String paymentNo;
    
    /**
     * 流程步骤
     */
    private String step;
    
    /**
     * 明细列表
     */
    private List<ItemDetail> items;
    
    @Data
    public static class ItemDetail {
        private Long itemId;
        private String itemCode;
        private String itemName;
        private String specification;
        private Integer quantity;
        private java.math.BigDecimal unitPrice;
        private java.math.BigDecimal totalAmount;
    }
    
    public PurchaseToAssetEvent() {
        super();
        this.setEventType("PURCHASE_TO_ASSET");
        this.setBusinessCenter("PURCHASE");
    }
}

