package com.coal.erp.business.domain.maintenance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 移动端签到记录表
 */
@Data
@TableName("maintenance_mobile_checkin")
public class MaintenanceMobileCheckin implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long checkinId;
    
    private Long workOrderId;
    
    private Long technicianId;
    
    private String technicianName;
    
    private String checkinType;
    
    private Date checkinTime;
    
    private String location;
    
    private BigDecimal latitude;
    
    private BigDecimal longitude;
    
    private String photoUrl;
    
    private String remark;
}

