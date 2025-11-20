package com.coal.erp.business.mapper.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.finance.FinancePayment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收付款单Mapper
 */
@Mapper
public interface FinancePaymentMapper extends BaseMapper<FinancePayment> {
}