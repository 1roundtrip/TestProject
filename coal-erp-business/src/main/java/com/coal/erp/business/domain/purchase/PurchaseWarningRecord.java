package com.coal.erp.business.domain.purchase;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 采购预警记录
 */
@Data
@TableName("purchase_warning_record")
public class PurchaseWarningRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long warningId;
    
    private String warningType;
    
    private String warningLevel; // HIGH-高, MEDIUM-中, LOW-低
    
    private Long orderId;
    
    private String orderNo;
    
    private Long supplierId;
    
    private String supplierName;
    
    private String warningContent;
    
    private Date warningDate;
    
    private String status; // PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, IGNORED-已忽略
    
    private Long handleUserId;
    
    private String handleUserName;
    
    private Date handleTime;
    
    private String handleResult;
    
    private Date createTime;
}

