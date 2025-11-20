package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningNotification;
import com.coal.erp.common.core.domain.R;

/**
 * 预警通知服务接口
 */
public interface IWarningNotificationService extends IService<WarningNotification> {
    
    Page<WarningNotification> pageNotification(Long current, Long size, Long recordId, String channelType, String sendStatus);
    
    R<?> resendNotification(Long notificationId);
}

