package com.coal.erp.business.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 维修业务流事件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MaintenanceBusinessEvent extends BusinessEvent {
    
    /**
     * 预警记录ID
     */
    private Long warningRecordId;
    
    /**
     * 维修工单ID
     */
    private Long workOrderId;
    
    /**
     * 维修工单号
     */
    private String workOrderNo;
    
    /**
     * 设备ID
     */
    private Long equipmentId;
    
    /**
     * 设备编号
     */
    private String equipmentCode;
    
    /**
     * 备件领用单ID
     */
    private Long requisitionId;
    
    /**
     * 备件领用单号
     */
    private String requisitionNo;
    
    /**
     * 维修费用ID
     */
    private Long costId;
    
    /**
     * 流程步骤
     */
    private String step;
    
    /**
     * 备件明细
     */
    private List<PartDetail> parts;
    
    @Data
    public static class PartDetail {
        private Long materialId;
        private String materialCode;
        private String materialName;
        private Integer quantity;
        private java.math.BigDecimal unitPrice;
        private java.math.BigDecimal totalAmount;
    }
    
    public MaintenanceBusinessEvent() {
        super();
        this.setEventType("MAINTENANCE_BUSINESS");
        this.setBusinessCenter("MAINTENANCE");
    }
}

