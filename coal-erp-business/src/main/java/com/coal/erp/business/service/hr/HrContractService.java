package com.coal.erp.business.service.hr;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.hr.HrContract;
import com.coal.erp.common.core.domain.R;

/**
 * 合同服务接口
 */
public interface HrContractService extends IService<HrContract> {
    /**
     * 创建劳动合同
     */
    R<?> createContract(HrContract contract);
    
    /**
     * 更新劳动合同
     */
    R<?> updateContract(HrContract contract);
    
    /**
     * 终止劳动合同
     */
    R<?> terminateContract(Long contractId, String reason);
    
    /**
     * 续签劳动合同
     */
    R<?> renewContract(Long contractId, Integer renewPeriod);
    
    /**
     * 校验合同编号唯一性
     */
    boolean checkContractNoUnique(HrContract contract);
    
    /**
     * 获取员工合同列表
     */
    R<?> getEmployeeContracts(Long employeeId);
    
    /**
     * 获取即将到期合同
     */
    R<?> getExpiringContracts(int days);
}