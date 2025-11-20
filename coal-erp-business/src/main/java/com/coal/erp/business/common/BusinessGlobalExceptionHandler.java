package com.coal.erp.business.common;

import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 业务模块全局异常处理器
 * 补充处理业务模块特定异常
 * 注意：通用异常已在 common 模块的 GlobalExceptionHandler 中处理
 */
@RestControllerAdvice(basePackages = "com.coal.erp.business")
public class BusinessGlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(BusinessGlobalExceptionHandler.class);
    
    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public R<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常: {}", e.getMessage());
        return R.error(ErrorCode.INVALID_PARAMETER.getCode(), e.getMessage());
    }
    
    /**
     * 处理业务规则异常
     */
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }
}

