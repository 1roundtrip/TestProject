package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证主表
 */
@Data
@TableName("voucher")
public class Voucher implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long voucherId;
    
    private String voucherNo;
    
    private Date voucherDate;
    
    private String period;
    
    private Integer attachCount;
    
    private BigDecimal totalAmount;
    
    private Long makerId;
    
    private String makerName;
    
    private Date makerTime;
    
    private Long auditorId;
    
    private String auditorName;
    
    private Date auditorTime;
    
    private Long posterId;
    
    private String posterName;
    
    private Date posterTime;
    
    private transient List<VoucherDetail> details;

    public List<VoucherDetail> getDetails() {
        return details;
    }

    public void setDetails(List<VoucherDetail> details) {
        this.details = details;
    }
    private String status;
    
    private String remark;
    
    private Date createTime;
    
    private Date updateTime;
}