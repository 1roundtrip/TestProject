package com.coal.erp.business.mapper.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.finance.FinancePayable;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应付单据Mapper
 */
@Mapper
public interface FinancePayableMapper extends BaseMapper<FinancePayable> {
}