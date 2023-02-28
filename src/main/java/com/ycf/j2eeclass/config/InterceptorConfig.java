package com.ycf.j2eeclass.config;

import com.ycf.j2eeclass.security.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/user/**") // 拦截的请求  表示拦截user下所有
                .addPathPatterns("/postman/**")
                .addPathPatterns("admin/**")
                .addPathPatterns("/info/**")
                .addPathPatterns("/pwd/**")
                .excludePathPatterns("/login") // 不拦截的请求  如登录接口
                .excludePathPatterns("/register");
    }
}
