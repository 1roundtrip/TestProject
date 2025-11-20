package com.coal.erp.business.mapper.purchase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.purchase.PurchasePayment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购付款Mapper
 */
@Mapper
public interface PurchasePaymentMapper extends BaseMapper<PurchasePayment> {
}

