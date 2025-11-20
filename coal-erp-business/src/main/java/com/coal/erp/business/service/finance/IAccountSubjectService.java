package com.coal.erp.business.service.finance;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coal.erp.business.domain.finance.AccountSubject;

/**
 * 会计科目服务接口
 */
public interface IAccountSubjectService extends IService<AccountSubject> {
    /**
     * 校验科目编码是否唯一
     */
    boolean checkSubjectCodeUnique(AccountSubject accountSubject);
}