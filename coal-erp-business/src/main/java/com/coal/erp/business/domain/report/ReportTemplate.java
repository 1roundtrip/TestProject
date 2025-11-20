package com.coal.erp.business.domain.report;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 报表模板实体类
 */
@Data
@TableName("report_template")
public class ReportTemplate {
    
    @TableId(type = IdType.AUTO)
    private Long templateId;
    
    private String templateName;
    
    private String templateCode;
    
    private Long categoryId;
    
    private String description;
    
    private String thumbnail;
    
    private String configJson;
    
    private String dataSourceType;
    
    private String dataSourceConfig;
    
    private Integer status;
    
    private Integer isSystem;
    
    private Long createUserId;
    
    private Date createTime;
    
    private Date updateTime;
}
