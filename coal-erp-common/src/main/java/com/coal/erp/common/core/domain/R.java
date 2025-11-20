package com.coal.erp.common.core.domain;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Integer code;
    private String msg;
    private T data;
    
    public static <T> R<T> success() {
        return success(null);
    }
    
    public static <T> R<T> success(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }
    
    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }
    
    public static <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
    
    /**
     * 失败响应（别名方法，兼容旧代码）
     */
    public static <T> R<T> fail(String msg) {
        return error(msg);
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }
}











