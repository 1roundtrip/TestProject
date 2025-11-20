package com.coal.erp.business.domain.asset;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 资产领用退库管理
 */
@Data
@TableName("asset_borrow")
public class AssetBorrow implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long borrowId;
    
    private String borrowNo;
    
    private String borrowType; // BORROW-领用, RETURN-退库
    
    private Date borrowDate;
    
    private Long assetId;
    
    private String assetCode;
    
    private String assetName;
    
    private Long borrowerId;
    
    private String borrowerName;
    
    private Long borrowerDeptId;
    
    private String borrowerDeptName;
    
    private Date expectedReturnDate;
    
    private Date actualReturnDate;
    
    private String borrowReason;
    
    private String status; // BORROWED-已领用, RETURNED-已归还, OVERDUE-逾期
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}

