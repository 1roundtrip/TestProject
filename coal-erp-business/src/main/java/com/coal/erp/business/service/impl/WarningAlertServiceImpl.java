package com.coal.erp.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.WarningAlert;
import com.coal.erp.business.mapper.WarningAlertMapper;
import com.coal.erp.business.service.IWarningAlertService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预警服务实现
 */
@Service
public class WarningAlertServiceImpl extends ServiceImpl<WarningAlertMapper, WarningAlert> implements IWarningAlertService {
    
    @Override
    public void createAlert(WarningAlert alert) {
        alert.setStatus("0"); // 0-未处理
        alert.setCreateTime(new java.util.Date());
        save(alert);
    }
    
    @Override
    public List<WarningAlert> getAlertsByLevel(String level) {
        LambdaQueryWrapper<WarningAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarningAlert::getAlertLevel, level)
               .eq(WarningAlert::getStatus, "0")
               .orderByDesc(WarningAlert::getCreateTime);
        return list(wrapper);
    }
    
    @Override
    public Long getUnhandledAlertCount() {
        LambdaQueryWrapper<WarningAlert> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarningAlert::getStatus, "0");
        return count(wrapper);
    }
    
    @Override
    public void markAsHandled(Long alertId) {
        WarningAlert alert = getById(alertId);
        if (alert != null) {
            alert.setStatus("1"); // 1-已处理
            alert.setUpdateTime(new java.util.Date());
            updateById(alert);
        }
    }
}















