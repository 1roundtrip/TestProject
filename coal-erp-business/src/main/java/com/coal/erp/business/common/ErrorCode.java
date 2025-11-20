package com.coal.erp.business.common;

/**
 * 统一错误码定义
 */
public enum ErrorCode {
    
    // 成功
    SUCCESS(200, "操作成功"),
    
    // 客户端错误 (400-499)
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "无权限"),
    NOT_FOUND(404, "资源不存在"),
    
    // 服务器错误 (500-599)
    INTERNAL_ERROR(500, "系统内部错误"),
    
    // 业务错误码 (1000-9999)
    BUSINESS_ERROR(1000, "业务处理失败"),
    DATA_NOT_FOUND(1001, "数据不存在"),
    DATA_ALREADY_EXISTS(1002, "数据已存在"),
    INVALID_STATUS(1003, "状态无效"),
    INVALID_PARAMETER(1004, "参数无效"),
    OPERATION_NOT_ALLOWED(1005, "操作不允许"),
    
    // 数据验证错误 (2000-2999)
    VALIDATION_ERROR(2000, "数据验证失败"),
    REQUIRED_FIELD_MISSING(2001, "必填字段缺失"),
    INVALID_FORMAT(2002, "格式无效"),
    
    // 业务规则错误 (3000-3999)
    BUSINESS_RULE_VIOLATION(3000, "违反业务规则"),
    STATUS_TRANSITION_INVALID(3001, "状态转换无效"),
    QUANTITY_INSUFFICIENT(3002, "数量不足"),
    
    // 集成错误 (4000-4999)
    INTEGRATION_ERROR(4000, "系统集成错误"),
    EXTERNAL_SERVICE_ERROR(4001, "外部服务错误"),
    EVENT_PUBLISH_FAILED(4002, "事件发布失败");
    
    private final Integer code;
    private final String message;
    
    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}

