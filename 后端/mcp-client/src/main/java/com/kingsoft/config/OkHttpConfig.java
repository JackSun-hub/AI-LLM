package com.kingsoft.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @Author sunjiacheng
 * @Date 2025/10/17 01:04
 * @PackageName:com.kingsoft.config
 * @ClassName: okHttpConfig
 * @Description: TODO
 * @Version 1.0
 */
@Configuration
public class OkHttpConfig implements WebMvcConfigurer {

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
