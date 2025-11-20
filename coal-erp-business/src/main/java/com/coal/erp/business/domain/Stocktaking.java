package com.coal.erp.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 盘点表
 */
@Data
@TableName("stocktaking")
public class Stocktaking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long stocktakingId;
    
    private String stocktakingNo;
    
    private String warehouse;
    
    private Date stocktakingDate;
    
    private String status;
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}















