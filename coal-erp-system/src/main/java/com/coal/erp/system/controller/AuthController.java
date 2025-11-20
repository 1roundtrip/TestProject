package com.coal.erp.system.controller;

import com.coal.erp.common.core.domain.R;
import com.coal.erp.common.core.exception.BusinessException;
import com.coal.erp.system.domain.SysUser;
import com.coal.erp.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private ISysUserService userService;
    
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> loginForm) {
        try {
            // 参数验证
            if (loginForm == null) {
                log.error("登录请求参数为空");
                return R.error("请求参数不能为空");
            }
            
            String username = loginForm.get("username");
            String password = loginForm.get("password");
            
            if (username == null || username.trim().isEmpty()) {
                log.error("用户名为空");
                return R.error("用户名不能为空");
            }
            
            if (password == null || password.trim().isEmpty()) {
                log.error("密码为空");
                return R.error("密码不能为空");
            }
            
            log.info("用户登录尝试: username={}", username);
            
            String token = userService.login(username.trim(), password);
            
            // 获取用户信息和权限
            SysUser user = userService.selectUserByUsername(username.trim());
            List<String> permissions = userService.selectPermsByUserId(user.getUserId());
            
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("username", user.getUsername());
            userInfo.put("nickName", user.getNickName() != null ? user.getNickName() : "");
            userInfo.put("avatar", user.getAvatar() != null ? user.getAvatar() : "");
            data.put("userInfo", userInfo);
            data.put("permissions", permissions);
            
            log.info("用户登录成功: username={}, permissions={}", username, permissions.size());
            return R.success(data);
            
        } catch (BusinessException e) {
            log.warn("登录业务异常: {}", e.getMessage());
            return R.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("登录系统异常", e);
            return R.error("登录失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public R<?> logout() {
        // TODO: 清除token缓存
        return R.success();
    }
}


