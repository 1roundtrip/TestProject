package com.coal.erp.system.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Web MVC配置
 * 由于排除了 WebMvcAutoConfiguration，需要手动配置消息转换器等
 * 这是一个纯API服务，不需要静态资源处理
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    /**
     * 配置ObjectMapper Bean
     * 用于JSON序列化和反序列化
     */
    @Bean
    @NonNull
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 注册Java 8时间模块
        mapper.registerModule(new JavaTimeModule());
        // 禁用将日期写为时间戳
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 忽略未知属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    
    /**
     * 配置消息转换器
     * 由于排除了 WebMvcAutoConfiguration，需要手动配置所有必要的转换器
     */
    @Override
    public void configureMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
        // 添加Jackson JSON消息转换器（最重要，用于处理JSON请求和响应）
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper());
        converters.add(jsonConverter);
        
        // 添加字符串转换器（用于处理纯文本响应）
        StringHttpMessageConverter stringConverter = 
            new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(stringConverter);
    }
    
    /**
     * 配置 RequestMappingHandlerAdapter
     * 确保消息转换器被正确注册
     */
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        
        // 添加Jackson JSON消息转换器
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(objectMapper());
        messageConverters.add(jsonConverter);
        
        // 添加字符串转换器
        StringHttpMessageConverter stringConverter = 
            new StringHttpMessageConverter(StandardCharsets.UTF_8);
        messageConverters.add(stringConverter);
        
        adapter.setMessageConverters(messageConverters);
        return adapter;
    }
}

