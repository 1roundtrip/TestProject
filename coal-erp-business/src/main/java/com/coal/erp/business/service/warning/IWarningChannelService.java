package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningChannel;
import com.coal.erp.common.core.domain.R;

/**
 * 预警渠道服务接口
 */
public interface IWarningChannelService extends IService<WarningChannel> {
    
    R<?> updateChannel(WarningChannel channel);
    
    R<?> enableChannel(Long channelId, Integer isEnabled);
    
    Page<WarningChannel> pageChannel(Long current, Long size, String channelCode, String channelName, String channelType);
}

