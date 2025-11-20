package com.coal.erp.business.mapper.finance;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.finance.VoucherDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 凭证明细Mapper接口
 */
public interface VoucherDetailMapper extends BaseMapper<VoucherDetail> {

    /**
     * 查询指定科目期间的金额合计
     */
    @Select("SELECT COALESCE(SUM(amount), 0) FROM voucher_detail " +
            "WHERE subject_id = #{subjectId} " +
            "AND DATE_FORMAT(create_time, '%Y%m') = #{period} " +
            "AND direction = #{direction}")
    BigDecimal selectSumAmount(@Param("subjectId") Long subjectId,
                              @Param("period") String period,
                              @Param("direction") String direction);

    /**
     * 查询指定期间有发生额的科目ID
     */
    @Select("SELECT DISTINCT subject_id FROM voucher_detail " +
            "WHERE DATE_FORMAT(create_time, '%Y%m') = #{period}")
    List<Long> selectDistinctSubjectIds(@Param("period") String period);
}