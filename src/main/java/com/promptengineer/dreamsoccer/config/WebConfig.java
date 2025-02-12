package com.promptengineer.dreamsoccer.config;
/*
IntelliJ IDEA 2024.3.2.2 (Community Edition)
Build #IC-243.23654.189, built on January 29, 2025
@Author TGS a.k.a. Dwitio Ahmad Pranoto
Java Developer
Created on 12/02/2025 9:47
@Last Modified 12/02/2025 9:47
Version 1.0
*/

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/event/**")
                .addResourceLocations("file:src/main/resources/static/uploads/event");
    }
}
