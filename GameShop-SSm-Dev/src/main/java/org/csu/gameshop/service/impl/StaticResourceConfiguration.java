package org.csu.gameshop.service.impl;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射外部目录 D:upload_images 到 URL /images/**
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:D:/upload_images/");
    }
}