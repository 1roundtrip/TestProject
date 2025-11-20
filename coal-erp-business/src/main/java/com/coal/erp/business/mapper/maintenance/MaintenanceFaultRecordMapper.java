package com.coal.erp.business.mapper.maintenance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.maintenance.MaintenanceFaultRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备故障记录Mapper
 */
@Mapper
public interface MaintenanceFaultRecordMapper extends BaseMapper<MaintenanceFaultRecord> {
}

