package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 收付款核销明细
 */
@Data
@TableName("finance_payment_settlement")
public class FinancePaymentSettlement implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long settlementId;
    
    private Long paymentId;
    
    private String sourceType;
    
    private Long sourceId;
    
    private String sourceNo;
    
    private BigDecimal settleAmount;
    
    private Date createTime;
}