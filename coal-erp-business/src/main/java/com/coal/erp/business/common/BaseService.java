package com.coal.erp.business.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础Service
 * 提供统一的Service方法和规范
 */
public abstract class BaseService {
    
    protected final Logger log = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 统一分页查询方法模板
     */
    protected abstract <T> Page<T> pageEntity(Long current, Long size, Object... filters);
    
    /**
     * 记录业务操作日志
     */
    protected void logBusinessOperation(String operation, Object... params) {
        log.info("业务操作: {}, 参数: {}", operation, params);
    }
    
    /**
     * 记录业务操作成功日志
     */
    protected void logBusinessSuccess(String operation, Object result) {
        log.info("业务操作成功: {}, 结果: {}", operation, result);
    }
    
    /**
     * 记录业务操作失败日志
     */
    protected void logBusinessError(String operation, Exception e) {
        log.error("业务操作失败: {}", operation, e);
    }
    
    /**
     * 统一异常处理
     */
    protected R<?> handleException(String operation, Exception e) {
        logBusinessError(operation, e);
        return R.error(operation + "失败: " + e.getMessage());
    }
}

