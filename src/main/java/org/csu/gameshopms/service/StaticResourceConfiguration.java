package org.csu.gameshopms.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射外部目录 D:upload_images 到 URL /images/**
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:D:/upload_images/");
    }
}