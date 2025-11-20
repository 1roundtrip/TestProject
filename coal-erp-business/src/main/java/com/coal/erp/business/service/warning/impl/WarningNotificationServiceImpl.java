package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningNotification;
import com.coal.erp.business.mapper.warning.WarningNotificationMapper;
import com.coal.erp.business.service.warning.IWarningNotificationService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警通知服务实现
 */
@Service
public class WarningNotificationServiceImpl extends ServiceImpl<WarningNotificationMapper, WarningNotification>
        implements IWarningNotificationService {
    
    @Override
    public Page<WarningNotification> pageNotification(Long current, Long size, Long recordId, String channelType, String sendStatus) {
        Page<WarningNotification> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningNotification> wrapper = new LambdaQueryWrapper<>();
        
        if (recordId != null) {
            wrapper.eq(WarningNotification::getRecordId, recordId);
        }
        if (channelType != null && !channelType.isEmpty()) {
            wrapper.eq(WarningNotification::getChannelType, channelType);
        }
        if (sendStatus != null && !sendStatus.isEmpty()) {
            wrapper.eq(WarningNotification::getSendStatus, sendStatus);
        }
        
        wrapper.orderByDesc(WarningNotification::getCreateTime);
        return page(page, wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> resendNotification(Long notificationId) {
        try {
            WarningNotification notification = getById(notificationId);
            if (notification == null) {
                return R.error("通知记录不存在");
            }
            notification.setSendStatus("PENDING");
            notification.setRetryCount(notification.getRetryCount() + 1);
            notification.setUpdateTime(new Date());
            updateById(notification);
            // TODO: 实际发送通知逻辑
            return R.success();
        } catch (Exception e) {
            return R.error("重发失败：" + e.getMessage());
        }
    }
}

