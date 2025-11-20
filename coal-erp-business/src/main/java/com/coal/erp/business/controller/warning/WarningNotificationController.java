package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningNotification;
import com.coal.erp.business.service.warning.IWarningNotificationService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警通知管理控制器
 */
@RestController
@RequestMapping("/api/warning/notification")
public class WarningNotificationController {
    
    @Autowired
    private IWarningNotificationService notificationService;
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:notification:list')")
    public R<Page<WarningNotification>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long recordId,
            @RequestParam(required = false) String channelType,
            @RequestParam(required = false) String sendStatus) {
        return R.success(notificationService.pageNotification(current, size, recordId, channelType, sendStatus));
    }
    
    @PostMapping
    @PreAuthorize("hasPermission(null, 'warning:notification:add')")
    public R<?> create(@RequestBody WarningNotification notification) {
        notification.setSendStatus("PENDING");
        notification.setRetryCount(0);
        notification.setCreateTime(new java.util.Date());
        notification.setUpdateTime(new java.util.Date());
        boolean success = notificationService.save(notification);
        if (success) {
            return R.success();
        } else {
            return R.error("创建失败");
        }
    }
    
    @PostMapping("/{id}/resend")
    @PreAuthorize("hasPermission(null, 'warning:notification:resend')")
    public R<?> resend(@PathVariable Long id) {
        return notificationService.resendNotification(id);
    }
}

