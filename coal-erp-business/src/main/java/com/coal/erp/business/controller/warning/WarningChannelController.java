package com.coal.erp.business.controller.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.domain.warning.WarningChannel;
import com.coal.erp.business.service.warning.IWarningChannelService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警渠道管理控制器
 */
@RestController
@RequestMapping("/api/warning/channel")
public class WarningChannelController {
    
    @Autowired
    private IWarningChannelService channelService;
    
    @PutMapping
    @PreAuthorize("hasPermission(null, 'warning:channel:edit')")
    public R<?> update(@RequestBody WarningChannel channel) {
        // 清除不允许修改的字段，防止前端传递这些字段导致错误
        channel.setChannelCode(null);
        channel.setChannelType(null);
        return channelService.updateChannel(channel);
    }
    
    @GetMapping("/page")
    @PreAuthorize("hasPermission(null, 'warning:channel:list')")
    public R<Page<WarningChannel>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String channelCode,
            @RequestParam(required = false) String channelName,
            @RequestParam(required = false) String channelType) {
        return R.success(channelService.pageChannel(current, size, channelCode, channelName, channelType));
    }
    
    @GetMapping("/{id}")
    public R<WarningChannel> getById(@PathVariable Long id) {
        return R.success(channelService.getById(id));
    }
    
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasPermission(null, 'warning:channel:enable')")
    public R<?> enable(@PathVariable Long id, @RequestParam Integer isEnabled) {
        return channelService.enableChannel(id, isEnabled);
    }
}

