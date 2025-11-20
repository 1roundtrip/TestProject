package com.coal.erp.business.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 库存管理流事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryBusinessEvent extends BusinessEvent {
    
    /**
     * 预警ID
     */
    private Long warningId;
    
    /**
     * 预警类型
     */
    private String warningType;
    
    /**
     * 采购申请ID
     */
    private Long purchaseRequisitionId;
    
    /**
     * 采购申请单号
     */
    private String purchaseRequisitionNo;
    
    /**
     * 入库单ID
     */
    private Long inboundId;
    
    /**
     * 入库单号
     */
    private String inboundNo;
    
    /**
     * 出库单ID
     */
    private Long outboundId;
    
    /**
     * 出库单号
     */
    private String outboundNo;
    
    /**
     * 调拨单ID
     */
    private Long transferId;
    
    /**
     * 调拨单号
     */
    private String transferNo;
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 仓库名称
     */
    private String warehouseName;
    
    /**
     * 流程步骤
     */
    private String step;
    
    /**
     * 物料明细
     */
    private List<MaterialDetail> materials;
    
    @Data
    public static class MaterialDetail {
        private Long materialId;
        private String materialCode;
        private String materialName;
        private Integer quantity;
        private java.math.BigDecimal currentStock;
        private java.math.BigDecimal safetyStock;
        private java.math.BigDecimal minStock;
    }
    
    public InventoryBusinessEvent() {
        super();
        this.setEventType("INVENTORY_BUSINESS");
        this.setBusinessCenter("INVENTORY");
    }
}

