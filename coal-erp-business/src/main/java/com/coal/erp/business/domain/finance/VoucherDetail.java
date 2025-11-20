package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证明细表
 */
@Data
@TableName("voucher_detail")
public class VoucherDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long detailId;
    
    private Long voucherId;
    
    private Integer entryNo;
    
    private Long subjectId;
    
    private String subjectCode;
    
    private String subjectName;
    
    private String direction;
    
    private BigDecimal amount;
    
    private Long deptId;
    
    private Long projectId;
    
    private Long staffId;
    
    private String summary;
    
    private Date createTime;
}