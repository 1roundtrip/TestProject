package com.coal.erp.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final String secretKey = "coal-erp-api-secret-key-2025";

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // 从请求头获取token
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            try {
                // 解析JWT (使用新版本API)
                javax.crypto.SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();

                // 验证权限
                String path = exchange.getRequest().getPath().value();
                Object rolesObj = claims.get("roles");
                List<String> roles = rolesObj != null ? (List<String>) rolesObj : java.util.Collections.emptyList();
                
                if (!hasPermission(path, roles)) {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                // 添加用户信息到请求头
                exchange.getRequest().mutate()
                    .header("X-User-Id", claims.getSubject())
                    .header("X-User-Roles", roles.isEmpty() ? "" : String.join(",", roles))
                    .build();

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    private boolean hasPermission(String path, List<String> roles) {
        // 权限验证逻辑
        if (path.startsWith("/api/finance") && !roles.contains("FINANCE_ACCESS")) {
            return false;
        }
        if (path.startsWith("/api/hr") && !roles.contains("HR_ACCESS")) {
            return false;
        }
        return true;
    }

    public static class Config {
        // 可配置参数
    }
}