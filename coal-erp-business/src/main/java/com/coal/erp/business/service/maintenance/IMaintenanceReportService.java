package com.coal.erp.business.service.maintenance;

import com.coal.erp.common.core.domain.R;

import java.util.Map;

/**
 * 维修报表服务接口
 */
public interface IMaintenanceReportService {
    
    /**
     * 获取维修统计
     */
    R<Map<String, Object>> getStatistics(String startDate, String endDate);
    
    /**
     * 获取工单统计
     */
    R<Map<String, Object>> getWorkOrderStatistics(String startDate, String endDate);
    
    /**
     * 获取成本统计
     */
    R<Map<String, Object>> getCostStatistics(String startDate, String endDate);
    
    /**
     * 获取故障统计
     */
    R<Map<String, Object>> getFaultStatistics(String startDate, String endDate);
}

