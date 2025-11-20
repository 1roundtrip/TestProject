package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 科目余额表
 */
@Data
@TableName("account_balance")
public class AccountBalance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long balanceId;
    
    private Long subjectId;
    
    private String subjectCode;
    
    private String subjectName;
    
    private String period;
    
    private String beginDirection;
    
    private BigDecimal beginAmount;
    
    private BigDecimal debitAmount;
    
    private BigDecimal creditAmount;
    
    private String endDirection;
    
    private BigDecimal endAmount;
    
    private Date createTime;
    
    private Date updateTime;
}