package com.coal.erp.common.core.exception;

import com.coal.erp.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        log.error("系统异常", e);
        // 打印完整的堆栈跟踪以便调试
        e.printStackTrace();
        String message = e.getMessage();
        if (message == null || message.isEmpty()) {
            message = e.getClass().getSimpleName();
        }
        return R.error("系统异常：" + message);
    }
    
    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        log.error("业务异常", e);
        return R.error(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public R<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error("权限不足", e);
        return R.error(403, "权限不足");
    }
    
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public R<?> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        log.error("未登录", e);
        return R.error(401, "未登录或登录已过期");
    }
}


