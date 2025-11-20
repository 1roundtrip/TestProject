package com.coal.erp.business.common;

/**
 * 业务异常工具类
 * 提供业务异常创建辅助方法
 * 注意：实际异常类使用 common 模块的 BusinessException
 */
public class BusinessExceptionHelper {
    
    /**
     * 创建业务异常（使用ErrorCode）
     */
    public static com.coal.erp.common.core.exception.BusinessException create(ErrorCode errorCode) {
        return new com.coal.erp.common.core.exception.BusinessException(errorCode.getCode(), errorCode.getMessage());
    }
    
    /**
     * 创建业务异常（使用ErrorCode和自定义消息）
     */
    public static com.coal.erp.common.core.exception.BusinessException create(ErrorCode errorCode, String customMessage) {
        return new com.coal.erp.common.core.exception.BusinessException(errorCode.getCode(), customMessage);
    }
    
    /**
     * 创建业务异常（使用自定义code和message）
     */
    public static com.coal.erp.common.core.exception.BusinessException create(Integer code, String message) {
        return new com.coal.erp.common.core.exception.BusinessException(code, message);
    }
}

