package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenanceMobileCheckin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 移动端签到记录Mapper
 */
@Mapper
public interface MaintenanceMobileCheckinMapper extends BaseMapper<MaintenanceMobileCheckin> {
}

