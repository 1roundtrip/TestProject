package com.coal.erp.business.service.warning;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.warning.WarningStatistics;
import com.coal.erp.common.core.domain.R;

import java.util.Date;

/**
 * 预警统计服务接口
 */
public interface IWarningStatisticsService extends IService<WarningStatistics> {
    
    Page<WarningStatistics> pageStatistics(Long current, Long size, Date startDate, Date endDate, String warningType, Long warningLevelId);
    
    R<?> getStatisticsSummary(Date startDate, Date endDate, String warningType);
}

