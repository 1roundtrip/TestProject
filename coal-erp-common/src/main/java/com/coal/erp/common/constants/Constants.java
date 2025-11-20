package com.coal.erp.common.constants;

/**
 * 系统常量
 */
public class Constants {
    
    /** 成功状态码 */
    public static final Integer SUCCESS = 200;
    
    /** 失败状态码 */
    public static final Integer FAIL = 500;
    
    /** 登录用户token key */
    public static final String TOKEN_HEADER = "Authorization";
    
    /** 登录用户token前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";
    
    /** 登录用户 redis key */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /** 权限缓存key */
    public static final String PERMISSION_KEY = "permission:";
    
    /** 菜单类型-目录 */
    public static final String MENU_TYPE_DIR = "M";
    
    /** 菜单类型-菜单 */
    public static final String MENU_TYPE_MENU = "C";
    
    /** 菜单类型-按钮 */
    public static final String MENU_TYPE_BUTTON = "F";
    
    /** 设备状态-正常 */
    public static final String ASSET_STATUS_NORMAL = "0";
    
    /** 设备状态-维修中 */
    public static final String ASSET_STATUS_REPAIRING = "1";
    
    /** 设备状态-报废 */
    public static final String ASSET_STATUS_SCRAPPED = "2";
    
    /** 防爆设备到期预警天数 */
    public static final Integer EXPLOSION_PROOF_WARNING_DAYS = 30;
}















