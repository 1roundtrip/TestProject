package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenanceStatistics;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修统计汇总Mapper
 */
@Mapper
public interface MaintenanceStatisticsMapper extends BaseMapper<MaintenanceStatistics> {
}

