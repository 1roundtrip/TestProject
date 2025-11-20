package com.coal.erp.business.service.report;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.report.ReportTemplate;
import com.coal.erp.common.core.domain.R;

import java.util.Map;

/**
 * 报表服务接口
 */
public interface IReportService extends IService<ReportTemplate> {

    /**
     * 执行报表查询
     * @param templateCode 模板编码
     * @param params 查询参数
     * @return 报表数据
     */
    R<?> executeQuery(String templateCode, Map<String, Object> params);

    /**
     * 获取报表配置
     * @param templateCode 模板编码
     * @return 配置信息
     */
    R<?> getConfig(String templateCode);

    /**
     * 保存报表配置
     * @param templateCode 模板编码
     * @param config 配置内容
     * @return 保存结果
     */
    R<?> saveConfig(String templateCode, String config);

    /**
     * 获取可用维度列表
     * @return 维度列表
     */
    R<?> listDimensions();

    /**
     * 获取可用指标列表
     * @return 指标列表
     */
    R<?> listMetrics();

    /**
     * 预置模板初始化
     * @return 初始化结果
     */
    R<?> initSystemTemplates();
}