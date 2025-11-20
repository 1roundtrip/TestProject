package com.coal.erp.business.domain.warning;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警处理记录表
 */
@Data
@TableName("warning_handle_record")
public class WarningHandleRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long handleId;
    
    private Long recordId;
    
    private String handleType;
    
    private Long handlerId;
    
    private String handlerName;
    
    private String handleAction;
    
    private String handleContent;
    
    private String handleAttachment;
    
    private Long nextHandlerId;
    
    private String nextHandlerName;
    
    private Date handleTime;
    
    private Date createTime;
}

