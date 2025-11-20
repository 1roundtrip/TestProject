package com.coal.erp.business.service.warning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.warning.WarningLevel;
import com.coal.erp.business.mapper.warning.WarningLevelMapper;
import com.coal.erp.business.service.warning.IWarningLevelService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 预警级别服务实现
 */
@Service
public class WarningLevelServiceImpl extends ServiceImpl<WarningLevelMapper, WarningLevel>
        implements IWarningLevelService {
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> createLevel(WarningLevel level) {
        try {
            LambdaQueryWrapper<WarningLevel> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(WarningLevel::getLevelCode, level.getLevelCode());
            if (count(wrapper) > 0) {
                return R.error("级别编码已存在");
            }
            
            level.setIsEnabled(level.getIsEnabled() == null ? 1 : level.getIsEnabled());
            level.setCreateTime(new Date());
            level.setUpdateTime(new Date());
            
            save(level);
            return R.success(level);
        } catch (Exception e) {
            return R.error("创建级别失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<?> updateLevel(WarningLevel level) {
        try {
            level.setUpdateTime(new Date());
            updateById(level);
            return R.success();
        } catch (Exception e) {
            return R.error("更新级别失败：" + e.getMessage());
        }
    }
    
    @Override
    public Page<WarningLevel> pageLevel(Long current, Long size, String levelCode, String levelName) {
        Page<WarningLevel> page = new Page<>(current, size);
        LambdaQueryWrapper<WarningLevel> wrapper = new LambdaQueryWrapper<>();
        
        if (levelCode != null && !levelCode.isEmpty()) {
            wrapper.like(WarningLevel::getLevelCode, levelCode);
        }
        if (levelName != null && !levelName.isEmpty()) {
            wrapper.like(WarningLevel::getLevelName, levelName);
        }
        
        wrapper.orderByAsc(WarningLevel::getLevelOrder);
        return page(page, wrapper);
    }
}

