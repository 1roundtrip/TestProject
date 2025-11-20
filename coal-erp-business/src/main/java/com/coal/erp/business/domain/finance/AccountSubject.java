package com.coal.erp.business.domain.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 会计科目表
 */
@Data
@TableName("account_subject")
public class AccountSubject implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long subjectId;
    
    private String subjectCode;
    
    private String subjectName;
    
    private Integer subjectLevel;
    
    private String subjectType;
    
    private String balanceDirection;
    
    private Long parentId;
    
    private String isLeaf;
    
    private String status;
    
    private Date createTime;
    
    private Date updateTime;
    
    private String remark;
}