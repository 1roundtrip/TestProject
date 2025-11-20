package com.coal.erp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 智慧煤矿ERP管理系统启动类
 * 
 * 排除 WebMvcAutoConfiguration 以避免 ServletContext 初始化问题
 * JSON消息转换器等由 WebMvcConfig 手动配置
 */
@SpringBootApplication(
    scanBasePackages = "com.coal.erp",
    exclude = {WebMvcAutoConfiguration.class}
)
@EnableScheduling
@MapperScan("com.coal.erp.**.mapper")
public class CoalErpApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(CoalErpApplication.class, args);
    }
}




