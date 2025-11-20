package com.coal.erp.system.security;

import com.coal.erp.common.constants.Constants;
import com.coal.erp.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // 跳过登录和登出接口，这些接口不需要JWT验证
        if (path != null && (path.contains("/api/auth/login") || path.contains("/api/auth/logout"))) {
            chain.doFilter(request, response);
            return;
        }
        
        String token = getTokenFromRequest(request);
        
        if (token != null && jwtUtils.validateToken(token)) {
            try {
            Claims claims = jwtUtils.getClaimsFromToken(token);
            String username = claims.getSubject();
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 记录权限信息用于调试
                if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
                    String authorities = userDetails.getAuthorities().stream()
                            .map(auth -> auth.getAuthority())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("");
                    logger.debug("用户 " + username + " 加载的权限: [" + authorities + "]");
                } else {
                    logger.warn("用户 " + username + " 没有加载到任何权限！");
                }
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token验证失败，但不阻止请求继续（由Spring Security处理）
                // 这样可以避免在登录接口时出现问题
            }
        }
        
        chain.doFilter(request, response);
    }
    
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(Constants.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

