package com.nutech.walletapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.nutech.walletapp.utils.JwtFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtFilter)
                .addPathPatterns("/api/**") // Terapkan ke semua endpoint API
                .excludePathPatterns("/api/auth/**"); // Kecualikan endpoint login
    }
}
