package com.itu.visabackoffice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${qr.code.path:src/main/resources/qr-codes}")
  private String qrCodePath;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173", "http://localhost:3000", "http://127.0.0.1:5173", "http://127.0.0.1:3000")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedHeaders("*")
        .allowCredentials(true)
        .maxAge(3600);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Serve QR codes from the configured directory
    registry.addResourceHandler("/qr-codes/**")
        .addResourceLocations("file:" + qrCodePath + "/")
        .setCachePeriod(86400); // Cache for 24 hours
  }
}
