package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningChannel;
import com.coal.erp.business.mapper.warning.WarningChannelMapper;
import com.coal.erp.business.service.warning.IWarningChannelService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警渠道服务实现
 */
@Service
public class WarningChannelServiceImpl extends ServiceImpl<WarningChannelMapper, WarningChannel>
        implements IWarningChannelService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateChannel(WarningChannel channel) {
        try {
            if (channel.getChannelId() == null) {
                return R.error("渠道ID不能为空");
            }
            
            // 先查询原记录，验证存在性
            WarningChannel existingChannel = getById(channel.getChannelId());
            if (existingChannel == null) {
                return R.error("渠道不存在");
            }
            
            // 使用 UpdateWrapper 只更新允许修改的字段，明确排除 channelCode 和 channelType
            LambdaUpdateWrapper<WarningChannel> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(WarningChannel::getChannelId, channel.getChannelId());
            
            // 只更新允许修改的字段（明确指定，不依赖传入对象的字段值）
            if (channel.getChannelName() != null) {
                updateWrapper.set(WarningChannel::getChannelName, channel.getChannelName());
            }
            if (channel.getChannelConfig() != null) {
                updateWrapper.set(WarningChannel::getChannelConfig, channel.getChannelConfig());
            }
            if (channel.getPriority() != null) {
                updateWrapper.set(WarningChannel::getPriority, channel.getPriority());
            }
            if (channel.getDailyLimit() != null) {
                updateWrapper.set(WarningChannel::getDailyLimit, channel.getDailyLimit());
            }
            if (channel.getRemark() != null) {
                updateWrapper.set(WarningChannel::getRemark, channel.getRemark());
            }
            if (channel.getIsEnabled() != null) {
                updateWrapper.set(WarningChannel::getIsEnabled, channel.getIsEnabled());
            }
            
            // 设置更新时间
            updateWrapper.set(WarningChannel::getUpdateTime, new Date());
            
            // 重要：明确不更新 channelCode 和 channelType（即使传入对象中有这些字段）
            // UpdateWrapper 只会更新通过 set() 方法指定的字段，所以这里不需要额外处理
            
            // 执行更新（使用 UpdateWrapper，不会更新 channelCode 和 channelType）
            boolean success = update(updateWrapper);
            if (success) {
                return R.success();
            } else {
                return R.error("更新失败");
            }
        } catch (Exception e) {
            return R.error("操作失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> enableChannel(Long channelId, Integer isEnabled) {
        try {
            WarningChannel channel = getById(channelId);
            if (channel == null) {
                return R.error("渠道不存在");
            }
            // 使用 UpdateWrapper 只更新 isEnabled 字段，避免更新其他字段
            LambdaUpdateWrapper<WarningChannel> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(WarningChannel::getChannelId, channelId);
            updateWrapper.set(WarningChannel::getIsEnabled, isEnabled);
            updateWrapper.set(WarningChannel::getUpdateTime, new Date());
            boolean success = update(updateWrapper);
            if (success) {
                return R.success();
            } else {
                return R.error("操作失败");
            }
        } catch (Exception e) {
            return R.error("操作失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<WarningChannel> pageChannel(Long current, Long size, String channelCode, String channelName, String channelType) {
        Page<WarningChannel> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningChannel> wrapper = new LambdaQueryWrapper<>();
        
        if (channelCode != null && !channelCode.isEmpty()) {
            wrapper.like(WarningChannel::getChannelCode, channelCode);
        }
        if (channelName != null && !channelName.isEmpty()) {
            wrapper.like(WarningChannel::getChannelName, channelName);
        }
        if (channelType != null && !channelType.isEmpty()) {
            wrapper.eq(WarningChannel::getChannelType, channelType);
        }
        
        wrapper.orderByAsc(WarningChannel::getPriority);
        return page(page, wrapper);
    }
}

