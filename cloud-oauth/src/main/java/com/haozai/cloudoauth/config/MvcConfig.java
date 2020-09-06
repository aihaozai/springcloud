package com.haozai.cloudoauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-09 21:37
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     *  自定义的登陆页面
     *  自定义的授权页面
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
       // registry.addViewController("/oauth/confirm_access").setViewName("oauth_approval");
    }
}
