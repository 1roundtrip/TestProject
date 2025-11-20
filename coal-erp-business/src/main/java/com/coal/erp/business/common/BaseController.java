package com.coal.erp.business.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础Controller
 * 提供统一的Controller方法和规范
 */
public abstract class BaseController {
    
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 统一分页查询方法模板
     * 子类应实现具体的分页查询逻辑
     */
    protected abstract <T> R<Page<T>> page(Long current, Long size, Object... filters);
    
    /**
     * 统一根据ID查询方法模板
     */
    protected abstract <T> R<T> getById(Long id);
    
    /**
     * 记录操作日志
     */
    protected void logOperation(String operation, Object... params) {
        log.info("执行操作: {}, 参数: {}", operation, params);
    }
    
    /**
     * 记录错误日志
     */
    protected void logError(String operation, Exception e) {
        log.error("操作失败: {}", operation, e);
    }
}

