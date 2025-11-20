package com.coal.erp.business.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.coal.erp.business.domain.Asset;
import com.coal.erp.business.domain.WarningAlert;
import com.coal.erp.business.service.IAssetService;
import com.coal.erp.business.service.INotificationService;
import com.coal.erp.business.service.IWarningAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 防爆设备到期预警定时任务
 */
@Component
public class ExplosionProofWarningTask {
    
    private static final Logger log = LoggerFactory.getLogger(ExplosionProofWarningTask.class);
    
    // 预警天数配置
    private static final int YELLOW_WARNING_DAYS = 30;  // 黄色预警：30天
    private static final int ORANGE_WARNING_DAYS = 7;   // 橙色预警：7天
    
    @Autowired
    private IAssetService assetService;
    
    @Autowired
    private IWarningAlertService warningAlertService;
    
    @Autowired
    private INotificationService notificationService;
    
    /**
     * 每天凌晨1点执行防爆设备到期检查
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkExplosionProofExpire() {
        log.info("========== 开始执行防爆设备到期预警任务 ==========");
        
        Date now = new Date();
        
        // 查询所有防爆设备
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Asset::getIsExplosionProof, "1")
               .isNotNull(Asset::getExplosionProofExpireDate)
               .orderByAsc(Asset::getExplosionProofExpireDate);
        
        List<Asset> explosionProofAssets = assetService.list(wrapper);
        
        if (explosionProofAssets.isEmpty()) {
            log.info("未发现防爆设备");
            return;
        }
        
        List<WarningAlert> alerts = new ArrayList<>();
        int yellowCount = 0;
        int orangeCount = 0;
        int redCount = 0;
        
        for (Asset asset : explosionProofAssets) {
            Date expireDate = asset.getExplosionProofExpireDate();
            if (expireDate == null) {
                continue;
            }
            
            // 计算剩余天数
            long daysDiff = (expireDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
            int daysRemaining = (int) daysDiff;
            
            String alertLevel = null;
            String alertTitle = null;
            String alertContent = null;
            
            // 判断预警级别
            if (daysRemaining < 0) {
                // 已到期：红色预警
                alertLevel = "RED";
                alertTitle = "【紧急】防爆设备证书已过期";
                alertContent = String.format("设备[%s]的防爆证书已于%s过期，请立即处理！", 
                    asset.getAssetName(), expireDate);
                redCount++;
            } else if (daysRemaining <= ORANGE_WARNING_DAYS) {
                // 7天内到期：橙色预警
                alertLevel = "ORANGE";
                alertTitle = "【重要】防爆设备证书即将到期";
                alertContent = String.format("设备[%s]的防爆证书将在%d天后（%s）到期，请尽快处理！", 
                    asset.getAssetName(), daysRemaining, expireDate);
                orangeCount++;
            } else if (daysRemaining <= YELLOW_WARNING_DAYS) {
                // 30天内到期：黄色预警
                alertLevel = "YELLOW";
                alertTitle = "【提醒】防爆设备证书即将到期";
                alertContent = String.format("设备[%s]的防爆证书将在%d天后（%s）到期，请注意及时处理。", 
                    asset.getAssetName(), daysRemaining, expireDate);
                yellowCount++;
            }
            
            // 创建预警记录
            if (alertLevel != null) {
                WarningAlert alert = new WarningAlert();
                alert.setAlertType("EXPLOSION_PROOF");
                alert.setAlertLevel(alertLevel);
                alert.setAssetId(asset.getAssetId());
                alert.setAssetCode(asset.getAssetCode());
                alert.setAssetName(asset.getAssetName());
                alert.setAlertTitle(alertTitle);
                alert.setAlertContent(alertContent);
                alert.setExpireDate(expireDate);
                alert.setDaysRemaining(daysRemaining);
                alert.setCreateTime(now);
                
                // 检查是否已存在相同预警（避免重复）
                if (!isDuplicateAlert(asset.getAssetId(), alertLevel)) {
                    warningAlertService.createAlert(alert);
                    alerts.add(alert);
                }
            }
        }
        
        // 发送通知
        if (!alerts.isEmpty()) {
            notificationService.sendBatchSystemNotification(alerts);
            log.warn("发现防爆设备预警：黄色预警{}个，橙色预警{}个，红色预警{}个", 
                yellowCount, orangeCount, redCount);
        } else {
            log.info("未发现需要预警的防爆设备");
        }
        
        log.info("========== 防爆设备到期预警任务执行完成 ==========");
    }
    
    /**
     * 检查是否已存在重复预警
     */
    private boolean isDuplicateAlert(Long assetId, String alertLevel) {
        // 查询今天是否已创建相同级别的预警
        List<WarningAlert> existingAlerts = warningAlertService.getAlertsByLevel(alertLevel);
        for (WarningAlert alert : existingAlerts) {
            if (alert.getAssetId().equals(assetId) && 
                alert.getCreateTime() != null &&
                isSameDay(alert.getCreateTime(), new Date())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断两个日期是否为同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * 每小时检查一次紧急预警（已到期设备）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void checkExpiredAssets() {
        log.info("开始检查已过期的防爆设备");
        
        Date now = new Date();
        LambdaQueryWrapper<Asset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Asset::getIsExplosionProof, "1")
               .lt(Asset::getExplosionProofExpireDate, now);
        
        List<Asset> expiredAssets = assetService.list(wrapper);
        
        if (!expiredAssets.isEmpty()) {
            log.error("发现{}个防爆设备已过期，需要立即处理！", expiredAssets.size());
            for (Asset asset : expiredAssets) {
                log.error("设备[{}]（编号：{}）防爆证书已于{}过期", 
                    asset.getAssetName(), asset.getAssetCode(), asset.getExplosionProofExpireDate());
            }
        }
    }
}
