package com.cjx.config;

import com.cjx.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于自定义Spring MVC的配置。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 注入LoginInterceptor，用于处理登录拦截逻辑
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 添加拦截器到Spring MVC的拦截器链中。
     * @param registry 用于注册拦截器的InterceptorRegistry对象
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 将loginInterceptor添加到拦截器链中，并排除登录和注册页面的拦截
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login", "/user/register");
    }
}
