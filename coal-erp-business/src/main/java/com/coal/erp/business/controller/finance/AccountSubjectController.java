package com.coal.erp.business.controller.finance;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coal.erp.business.config.FinanceSecurityConfig;
import com.coal.erp.business.domain.finance.AccountSubject;
import com.coal.erp.business.service.finance.IAccountSubjectService;
import com.coal.erp.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 会计科目控制器
 */
@RestController
@RequestMapping("/api/finance/subject")
@FinanceSecurityConfig.RequiresSubjectPermission
public class AccountSubjectController {

    @Autowired
    private IAccountSubjectService accountSubjectService;

    /**
     * 分页查询科目
     */
    @GetMapping("/page")
    public R<Page<AccountSubject>> page(@RequestParam(defaultValue = "1") Long current,
                                        @RequestParam(defaultValue = "10") Long size,
                                        @RequestParam(required = false) String subjectName,
                                        @RequestParam(required = false) String subjectType) {
        Page<AccountSubject> page = new Page<>(current, size);
        LambdaQueryWrapper<AccountSubject> wrapper = new LambdaQueryWrapper<>();
        
        if (subjectName != null && !subjectName.isEmpty()) {
            wrapper.like(AccountSubject::getSubjectName, subjectName);
        }
        if (subjectType != null && !subjectType.isEmpty()) {
            wrapper.eq(AccountSubject::getSubjectType, subjectType);
        }
        wrapper.orderByAsc(AccountSubject::getSubjectCode);

        return R.success(accountSubjectService.page(page, wrapper));
    }

    /**
     * 获取科目列表
     */
    @GetMapping("/list")
    public R<List<AccountSubject>> list(@RequestParam(required = false) String subjectType) {
        LambdaQueryWrapper<AccountSubject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountSubject::getStatus, "0");
        
        if (subjectType != null && !subjectType.isEmpty()) {
            wrapper.eq(AccountSubject::getSubjectType, subjectType);
        }
        wrapper.orderByAsc(AccountSubject::getSubjectCode);

        return R.success(accountSubjectService.list(wrapper));
    }

    /**
     * 根据ID获取科目
     */
    @GetMapping("/{subjectId}")
    public R<AccountSubject> getById(@PathVariable Long subjectId) {
        return R.success(accountSubjectService.getById(subjectId));
    }

    /**
     * 新增科目
     */
    @PostMapping
    @FinanceSecurityConfig.RequiresSubjectPermission("add")
    public R<?> add(@RequestBody AccountSubject accountSubject) {
        if (!accountSubjectService.checkSubjectCodeUnique(accountSubject)) {
            return R.fail("科目编码已存在");
        }
        return R.success(accountSubjectService.save(accountSubject));
    }

    /**
     * 修改科目
     */
    @PutMapping
    @FinanceSecurityConfig.RequiresSubjectPermission("edit")
    public R<?> update(@RequestBody AccountSubject accountSubject) {
        if (!accountSubjectService.checkSubjectCodeUnique(accountSubject)) {
            return R.fail("科目编码已存在");
        }
        return R.success(accountSubjectService.updateById(accountSubject));
    }

    /**
     * 删除科目
     */
    @DeleteMapping("/{subjectId}")
    @FinanceSecurityConfig.RequiresSubjectPermission("remove")
    public R<?> delete(@PathVariable Long subjectId) {
        // TODO: 检查科目是否被使用
        return R.success(accountSubjectService.removeById(subjectId));
    }

    /**
     * 获取科目类型枚举
     */
    @GetMapping("/types")
    public R<List<String>> getSubjectTypes() {
        List<String> types = Arrays.asList(
            "ASSET", "LIABILITY", "EQUITY", "COST", "PROFIT"
        );
        return R.success(types);
    }
}