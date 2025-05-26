package com.ticket.ticket_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/seats").setViewName("seat-selection");
        registry.addViewController("/ticket/success").setViewName("ticket-success");
        registry.addViewController("/user/orders").setViewName("user-orders");
    }
} 