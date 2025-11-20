package com.coal.erp.business.service.impl.finance;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.business.domain.finance.AccountSubject;
import com.coal.erp.business.mapper.finance.AccountSubjectMapper;
import com.coal.erp.business.service.finance.IAccountSubjectService;
import org.springframework.stereotype.Service;

/**
 * 会计科目服务实现
 */
@Service
public class AccountSubjectServiceImpl 
    extends ServiceImpl<AccountSubjectMapper, AccountSubject> 
    implements IAccountSubjectService {

    @Override
    public boolean checkSubjectCodeUnique(AccountSubject accountSubject) {
        Long subjectId = accountSubject.getSubjectId() == null ? -1L : accountSubject.getSubjectId();
        AccountSubject info = lambdaQuery()
            .eq(AccountSubject::getSubjectCode, accountSubject.getSubjectCode())
            .one();
        return info == null || info.getSubjectId().equals(subjectId);
    }
}