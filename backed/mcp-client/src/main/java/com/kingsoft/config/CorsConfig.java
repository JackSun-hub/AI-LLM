package com.kingsoft.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author sunjiacheng
 * @Date 2025/10/14 18:01
 * @PackageName:com.kingsoft.config
 * @ClassName: CorsConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
   @Value("${website.domain}")
    private String domain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")  // 允许所有域名（开发环境）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
