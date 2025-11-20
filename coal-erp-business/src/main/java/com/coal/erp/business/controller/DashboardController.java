package com.coal.erp.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.service.IAssetService;
import com.coal.erp.business.service.IWarningAlertService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘控制器
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    
    @Autowired
    private IAssetService assetService;
    
    @Autowired
    private IWarningAlertService warningAlertService;
    
    /**
     * 获取仪表盘统计数据
     */
    @GetMapping("/stats")
    public R<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 设备总数
        long totalAssets = assetService.count();
        stats.put("totalAssets", totalAssets);
        
        // 维修中的设备数（status = '1'）
        long repairingAssets = assetService.count(
            new LambdaQueryWrapper<Asset>().eq(Asset::getStatus, "1")
        );
        stats.put("repairingAssets", repairingAssets);
        
        // 库存预警（暂时返回0，后续可以根据库存表实现）
        stats.put("inventoryAlerts", 0);
        
        // 防爆预警数（未处理的防爆预警）
        long explosionProofWarnings = warningAlertService.getUnhandledAlertCount();
        stats.put("explosionProofWarnings", explosionProofWarnings);
        
        return R.success(stats);
    }
    
    /**
     * 获取设备状态分布
     */
    @GetMapping("/asset-status")
    public R<List<Map<String, Object>>> getAssetStatusDistribution() {
        // 正常设备数（status = '0'）
        long normalCount = assetService.count(
            new LambdaQueryWrapper<Asset>().eq(Asset::getStatus, "0")
        );
        
        // 维修中设备数（status = '1'）
        long repairingCount = assetService.count(
            new LambdaQueryWrapper<Asset>().eq(Asset::getStatus, "1")
        );
        
        // 报废设备数（status = '2'）
        long scrappedCount = assetService.count(
            new LambdaQueryWrapper<Asset>().eq(Asset::getStatus, "2")
        );
        
        List<Map<String, Object>> distribution = new ArrayList<>();
        Map<String, Object> normal = new HashMap<>();
        normal.put("name", "正常");
        normal.put("value", normalCount);
        distribution.add(normal);
        
        Map<String, Object> repairing = new HashMap<>();
        repairing.put("name", "维修中");
        repairing.put("value", repairingCount);
        distribution.add(repairing);
        
        Map<String, Object> scrapped = new HashMap<>();
        scrapped.put("name", "报废");
        scrapped.put("value", scrappedCount);
        distribution.add(scrapped);
        
        return R.success(distribution);
    }
    
    /**
     * 获取维修趋势（最近6个月）
     */
    @GetMapping("/repair-trend")
    public R<List<Map<String, Object>>> getRepairTrend() {
        // TODO: 根据实际维修记录表实现
        // 暂时返回空数据
        List<Map<String, Object>> trend = new ArrayList<>();
        return R.success(trend);
    }
}

