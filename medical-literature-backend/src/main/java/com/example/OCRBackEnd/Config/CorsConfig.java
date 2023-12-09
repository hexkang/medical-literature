
package com.example.OCRBackEnd.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")//允许所有域访问
                .allowedMethods("GET","POST","PUT","DELETE","HEAD","OPTIONS")//允许所有方法访问
                .allowCredentials(true)//允许携带cookie
                .maxAge(3600)//设置浏览器询问的有效期
                .allowedHeaders("*");//允许任意请求头
    }
}
