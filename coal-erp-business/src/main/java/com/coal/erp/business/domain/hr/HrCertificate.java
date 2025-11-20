package com.coal.erp.business.domain.hr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 证照管理表
 */
@Data
@TableName("hr_certificate")
public class HrCertificate implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long certificateId;
    
    private Long employeeId;
    
    private String certType;
    
    private String certName;
    
    private String certNumber;
    
    private String issueAuthority;
    
    private Date issueDate;
    
    private Date expireDate;
    
    private String certLevel;
    
    private String specialOperationType;
    
    private String attachmentPath;
    
    private String status;
    
    private Date reviewDate;
    
    private Date createTime;
    
    private Date updateTime;
}