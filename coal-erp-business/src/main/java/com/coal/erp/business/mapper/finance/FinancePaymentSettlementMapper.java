package com.coal.erp.business.mapper.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.finance.FinancePaymentSettlement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收付款核销明细Mapper
 */
@Mapper
public interface FinancePaymentSettlementMapper extends BaseMapper<FinancePaymentSettlement> {
}