package com.coal.erp.business.service.impl.hr;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.hr.HrContract;
import com.coal.erp.business.mapper.hr.HrContractMapper;
import com.coal.erp.business.service.hr.HrContractService;
import com.coal.erp.common.core.domain.R;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 合同服务实现
 */
@Service
public class HrContractServiceImpl 
    extends ServiceImpl<HrContractMapper, HrContract> 
    implements HrContractService {

    @Override
    public R<?> createContract(HrContract contract) {
        if (!checkContractNoUnique(contract)) {
            return R.fail("合同编号已存在");
        }
        
        // 设置默认状态
        if (contract.getStatus() == null) {
            contract.setStatus("ACTIVE");
        }
        
        // 自动计算合同期限
        if (contract.getStartDate() != null && contract.getEndDate() == null && contract.getContractPeriod() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(contract.getStartDate());
            calendar.add(Calendar.MONTH, contract.getContractPeriod());
            contract.setEndDate(calendar.getTime());
        }
        
        return R.success(save(contract));
    }

    @Override
    public R<?> updateContract(HrContract contract) {
        if (!checkContractNoUnique(contract)) {
            return R.fail("合同编号已存在");
        }
        
        return R.success(updateById(contract));
    }

    @Override
    public R<?> terminateContract(Long contractId, String reason) {
        HrContract contract = getById(contractId);
        if (contract == null) {
            return R.fail("合同不存在");
        }
        
        contract.setStatus("TERMINATED");
        if (contract.getRemark() == null) {
            contract.setRemark("终止原因：" + reason);
        } else {
            contract.setRemark(contract.getRemark() + "\n终止原因：" + reason);
        }
        
        return R.success(updateById(contract));
    }

    @Override
    public R<?> renewContract(Long contractId, Integer renewPeriod) {
        HrContract contract = getById(contractId);
        if (contract == null) {
            return R.fail("合同不存在");
        }
        
        // 创建新合同
        HrContract newContract = new HrContract();
        newContract.setEmployeeId(contract.getEmployeeId());
        newContract.setContractType(contract.getContractType());
        newContract.setSignDate(new Date());
        newContract.setStartDate(contract.getEndDate());
        newContract.setContractPeriod(renewPeriod);
        newContract.setTrialPeriod(0); // 续签无试用期
        newContract.setStatus("ACTIVE");
        newContract.setSalaryAmount(contract.getSalaryAmount());
        newContract.setWorkLocation(contract.getWorkLocation());
        newContract.setJobPosition(contract.getJobPosition());
        newContract.setRemark("续签合同，原合同编号：" + contract.getContractNo());
        
        // 生成新合同编号
        String newContractNo = generateContractNo(contract.getEmployeeId());
        newContract.setContractNo(newContractNo);
        
        // 终止原合同
        contract.setStatus("RENEWED");
        updateById(contract);
        
        return createContract(newContract);
    }

    @Override
    public boolean checkContractNoUnique(HrContract contract) {
        Long contractId = contract.getContractId() == null ? -1L : contract.getContractId();
        HrContract info = lambdaQuery()
            .eq(HrContract::getContractNo, contract.getContractNo())
            .one();
        return info == null || info.getContractId().equals(contractId);
    }

    @Override
    public R<?> getEmployeeContracts(Long employeeId) {
        List<HrContract> contracts = lambdaQuery()
            .eq(HrContract::getEmployeeId, employeeId)
            .orderByDesc(HrContract::getStartDate)
            .list();
        return R.success(contracts);
    }

    @Override
    public R<?> getExpiringContracts(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date targetDate = calendar.getTime();
        
        List<HrContract> contracts = lambdaQuery()
            .eq(HrContract::getStatus, "ACTIVE")
            .le(HrContract::getEndDate, targetDate)
            .orderByAsc(HrContract::getEndDate)
            .list();
        
        return R.success(contracts);
    }

    /**
     * 生成合同编号
     */
    private String generateContractNo(Long employeeId) {
        return "CT" + System.currentTimeMillis() + "-" + employeeId;
    }
}