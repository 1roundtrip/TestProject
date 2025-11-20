package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 维修工单明细表
 */
@Data
@TableName("maintenance_work_order_detail")
public class MaintenanceWorkOrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long workOrderId;
    
    private Integer stepNo;
    
    private String stepName;
    
    private String stepDescription;
    
    private Long technicianId;
    
    private String technicianName;
    
    private Date startTime;
    
    private Date endTime;
    
    private Integer duration;
    
    private String status;
    
    private String remark;
}

