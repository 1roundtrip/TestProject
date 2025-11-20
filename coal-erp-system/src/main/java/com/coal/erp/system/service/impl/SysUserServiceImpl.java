package com.coal.erp.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coal.erp.common.core.exception.BusinessException;
import com.coal.erp.common.utils.JwtUtils;
import com.coal.erp.system.domain.SysUser;
import com.coal.erp.system.mapper.SysMenuMapper;
import com.coal.erp.system.mapper.SysUserMapper;
import com.coal.erp.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private SysMenuMapper menuMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Override
    public SysUser selectUserByUsername(String username) {
        try {
            log.info("========== 开始查询用户 ==========");
            log.info("查询用户名: [{}]", username);
            log.info("用户名长度: {}", username != null ? username.length() : 0);
            log.info("用户名trim后: [{}]", username != null ? username.trim() : null);
            
            // 执行精确查询
            // 由于MyBatis-Plus的selectOne和原生SQL查询都有问题，我们使用selectList然后过滤
            SysUser user = null;
            
            // 方法1: 先尝试使用selectList然后过滤（最可靠的方法）
            try {
                log.info("尝试使用selectList查询所有用户，然后过滤匹配的用户名: [{}]", username);
                List<SysUser> allUsers = userMapper.selectList(null);
                log.info("数据库中总用户数: {}", allUsers != null ? allUsers.size() : 0);
                
                if (allUsers != null && !allUsers.isEmpty()) {
                    log.info("数据库中的所有用户名列表:");
                    for (SysUser u : allUsers) {
                        log.info("  - userId={}, username=[{}], usernameLength={}, status={}, passwordLength={}", 
                            u.getUserId(), u.getUsername(), 
                            u.getUsername() != null ? u.getUsername().length() : 0,
                            u.getStatus(),
                            u.getPassword() != null ? u.getPassword().length() : 0);
                        
                        // 直接匹配用户名
                        if (u.getUsername() != null && username != null) {
                            boolean exactMatch = u.getUsername().equals(username);
                            boolean trimMatch = u.getUsername().trim().equals(username.trim());
                            boolean caseInsensitiveMatch = u.getUsername().equalsIgnoreCase(username);
                            log.info("    对比结果: exactMatch={}, trimMatch={}, caseInsensitiveMatch={}", 
                                exactMatch, trimMatch, caseInsensitiveMatch);
                            
                            // 如果精确匹配，直接使用这个用户
                            if (exactMatch && user == null) {
                                user = u;
                                log.info("✓ 通过selectList过滤找到用户: userId={}, username=[{}]", user.getUserId(), user.getUsername());
                            }
                        }
                    }
                } else {
                    log.error("数据库中没有任何用户！");
                }
            } catch (Exception e) {
                log.error("查询所有用户时出错", e);
            }
            
            // 方法2: 如果方法1失败，尝试使用原生SQL查询
            if (user == null) {
                try {
                    log.info("方法1失败，尝试使用原生SQL查询用户: username=[{}]", username);
                    user = userMapper.selectByUsernameNative(username);
                    if (user != null) {
                        log.info("✓ 使用原生SQL找到用户: userId={}, username=[{}]", user.getUserId(), user.getUsername());
                    }
                } catch (Exception e) {
                    log.warn("原生SQL查询失败: {}", e.getMessage());
                }
            }
            
            // 方法3: 如果方法2失败，尝试使用MyBatis-Plus查询
            if (user == null) {
                try {
                    log.info("方法2失败，尝试使用MyBatis-Plus查询用户: username=[{}]", username);
                    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(SysUser::getUsername, username);
                    user = userMapper.selectOne(wrapper);
                    if (user != null) {
                        log.info("✓ 使用MyBatis-Plus找到用户: userId={}, username=[{}]", user.getUserId(), user.getUsername());
                    }
                } catch (Exception e) {
                    log.warn("MyBatis-Plus查询失败: {}", e.getMessage());
                }
            }
            
            // 方法4: 如果方法3失败，尝试不区分大小写查询
            if (user == null && username != null) {
                try {
                    log.info("方法3失败，尝试使用不区分大小写SQL查询用户: username=[{}]", username);
                    user = userMapper.selectByUsernameCaseInsensitive(username);
                    if (user != null) {
                        log.info("✓ 使用不区分大小写SQL找到用户: userId={}, username=[{}]", user.getUserId(), user.getUsername());
                    }
                } catch (Exception e) {
                    log.warn("不区分大小写SQL查询失败: {}", e.getMessage());
                }
            }
            
            if (user != null) {
                log.info("✓ 找到用户: userId={}, username=[{}], status={}, passwordLength={}", 
                    user.getUserId(), user.getUsername(), user.getStatus(),
                    user.getPassword() != null ? user.getPassword().length() : 0);
            } else {
                log.error("✗ 未找到用户: username=[{}]", username);
                log.error("可能的原因：");
                log.error("  1. 用户名不匹配（注意大小写和空格）");
                log.error("  2. 数据库连接到了错误的数据库");
                log.error("  3. 表名或字段名映射错误");
            }
            
            log.info("========== 查询用户结束 ==========");
            return user;
        } catch (Exception e) {
            log.error("查询用户异常: username={}, error={}", username, e.getMessage(), e);
            log.error("异常堆栈:", e);
            throw e;
        }
    }
    
    @Override
    public List<String> selectPermsByUserId(Long userId) {
        try {
            return menuMapper.selectPermsByUserId(userId);
        } catch (Exception e) {
            log.error("查询用户权限异常: userId={}", userId, e);
            // 如果查询权限失败，返回空列表而不是抛出异常
            return Collections.emptyList();
        }
    }
    
    @Override
    public String login(String username, String password) {
        try {
            log.info("开始登录验证: username={}", username);
            
            // 查询用户
            log.debug("准备查询用户: username={}", username);
            SysUser user = selectUserByUsername(username);
            
            // 添加更详细的日志
            if (user == null) {
                log.error("用户不存在: username={}", username);
                log.error("请检查：1. 数据库中是否存在该用户 2. 数据库连接是否正确 3. 表名是否正确");
                
                // 尝试查询所有用户（用于调试）
                try {
                    List<SysUser> allUsers = userMapper.selectList(null);
                    log.debug("数据库中所有用户数量: {}", allUsers != null ? allUsers.size() : 0);
                    if (allUsers != null && !allUsers.isEmpty()) {
                        log.debug("数据库中的用户列表: {}", 
                            allUsers.stream()
                                .map(u -> u.getUsername() + "(" + u.getUserId() + ")")
                                .collect(java.util.stream.Collectors.joining(", ")));
                    }
                } catch (Exception e) {
                    log.error("查询所有用户时出错", e);
                }
                
                throw new BusinessException(401, "用户名不存在，请检查用户名是否正确或联系管理员创建账户");
            }
            
            // 检查密码
            String storedPassword = user.getPassword();
            
            log.debug("找到用户: userId={}, username={}, status={}, passwordLength={}", 
                     user.getUserId(), user.getUsername(), user.getStatus(), 
                     storedPassword != null ? storedPassword.length() : 0);
            if (storedPassword == null || storedPassword.isEmpty()) {
                log.warn("用户密码未设置: username={}", username);
                throw new BusinessException(401, "用户密码未设置");
            }
            
            // 直接比较明文密码（内网环境简化版本）
            // 使用 trim() 去除可能的空格
            String trimmedPassword = password.trim();
            String trimmedStoredPassword = storedPassword.trim();
            
            if (!trimmedPassword.equals(trimmedStoredPassword)) {
                log.warn("密码错误: username={}, inputPassword={}, storedPassword={}", 
                        username, trimmedPassword, trimmedStoredPassword);
                throw new BusinessException(401, "密码错误");
            }
            
            log.debug("密码验证通过: username={}", username);
            
            // 检查用户状态
            String status = user.getStatus();
            if (status == null || !"0".equals(status)) {
                log.warn("用户已被禁用: username={}, status={}", username, status);
                throw new BusinessException(403, "用户已被禁用");
            }
            
            // 生成token
            if (user.getUserId() == null) {
                log.error("用户ID为空: username={}", username);
                throw new BusinessException("用户数据异常");
            }
            
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                log.error("用户名为空: userId={}", user.getUserId());
                throw new BusinessException("用户数据异常");
            }
            
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("username", user.getUsername());
            
            String token = jwtUtils.generateToken(claims, user.getUsername());
            log.info("登录成功: username={}, userId={}", username, user.getUserId());
            
            return token;
            
        } catch (BusinessException e) {
            // 业务异常直接抛出
            throw e;
        } catch (Exception e) {
            log.error("登录系统异常: username={}", username, e);
            throw new BusinessException("登录失败: " + e.getMessage());
        }
    }
    
    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            log.info("开始修改密码: userId={}", userId);
            
            // 查询用户
            SysUser user = getById(userId);
            if (user == null) {
                log.warn("用户不存在: userId={}", userId);
                throw new BusinessException(401, "用户不存在");
            }
            
            // 验证旧密码
            String storedPassword = user.getPassword();
            if (storedPassword == null || storedPassword.isEmpty()) {
                log.warn("用户密码未设置: userId={}", userId);
                throw new BusinessException(401, "用户密码未设置");
            }
            
            // 直接比较明文密码（内网环境简化版本）
            String trimmedOldPassword = oldPassword.trim();
            String trimmedStoredPassword = storedPassword.trim();
            
            if (!trimmedOldPassword.equals(trimmedStoredPassword)) {
                log.warn("原密码错误: userId={}", userId);
                throw new BusinessException(401, "原密码错误");
            }
            
            log.debug("原密码验证通过: userId={}", userId);
            
            // 验证新密码
            if (newPassword == null || newPassword.trim().isEmpty()) {
                log.warn("新密码不能为空: userId={}", userId);
                throw new BusinessException(400, "新密码不能为空");
            }
            
            String trimmedNewPassword = newPassword.trim();
            if (trimmedNewPassword.length() < 6) {
                log.warn("新密码长度不足: userId={}", userId);
                throw new BusinessException(400, "新密码长度不能少于6位");
            }
            
            // 更新密码
            user.setPassword(trimmedNewPassword);
            boolean updated = updateById(user);
            
            if (!updated) {
                log.error("密码更新失败: userId={}", userId);
                throw new BusinessException("密码更新失败");
            }
            
            log.info("密码修改成功: userId={}", userId);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改密码系统异常: userId={}", userId, e);
            throw new BusinessException("修改密码失败: " + e.getMessage());
        }
    }
}

