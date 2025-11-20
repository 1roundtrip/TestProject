package com.coal.erp.business.mapper.hr;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coal.erp.business.domain.hr.HrEmployee;
import com.coal.erp.business.handler.EncryptedFieldHandler;
import org.apache.ibatis.annotations.*;

/**
 * 员工Mapper接口
 */
@Mapper
public interface HrEmployeeMapper extends BaseMapper<HrEmployee> {
    @Results({
        @Result(column = "id_card", property = "idCard", typeHandler = EncryptedFieldHandler.class),
        @Result(column = "current_address", property = "currentAddress", typeHandler = EncryptedFieldHandler.class),
        @Result(column = "emergency_contact", property = "emergencyContact", typeHandler = EncryptedFieldHandler.class),
        @Result(column = "emergency_phone", property = "emergencyPhone", typeHandler = EncryptedFieldHandler.class)
    })
    @Select("SELECT * FROM hr_employee WHERE employee_id = #{employeeId}")
    HrEmployee selectEmployeeWithDecryption(Long employeeId);
}