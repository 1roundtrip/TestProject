package com.coal.erp.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.WarningAlert;

import java.util.List;

/**
 * 预警服务接口
 */
public interface IWarningAlertService extends IService<WarningAlert> {
    
    /**
     * 创建预警记录
     */
    void createAlert(WarningAlert alert);
    
    /**
     * 根据级别查询预警
     */
    List<WarningAlert> getAlertsByLevel(String level);
    
    /**
     * 获取未处理的预警数量
     */
    Long getUnhandledAlertCount();
    
    /**
     * 标记预警为已处理
     */
    void markAsHandled(Long alertId);
}















