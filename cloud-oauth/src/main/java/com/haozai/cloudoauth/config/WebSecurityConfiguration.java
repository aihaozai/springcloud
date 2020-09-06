package com.haozai.cloudoauth.config;

import com.haozai.cloudoauth.service.UserServiceDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @program: springCloud
 * @author: haozai
 * @description: EnableGlobalMethodSecurity开启方法级别支持spel表达式的角色权限校验注解，粒度更细，更好控制
 * @date: 2020-08-08 17:05
 **/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserServiceDetailImpl userServiceDetail;

    /**
     * set方法注入
     * @param userServiceDetail
     */
    @Autowired
    public WebSecurityConfiguration(UserServiceDetailImpl userServiceDetail) {
        this.userServiceDetail = userServiceDetail;
    }

    /**
     * AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
     * AccessDeniedHandler 用来解决认证过的用户访问无权限资源时的异常
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/oauth/**","/login","/health", "/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();
//        http.exceptionHandling()
//                .authenticationEntryPoint((request,response,authException)->response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                .accessDeniedHandler((request,response,authException)->response.sendError(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 需要配置这个支持password模式
     * support password grant type
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
    /**
     * 配置认证信息
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       // auth.parentAuthenticationManager(authenticationManagerBean());
        auth.userDetailsService(userServiceDetail).passwordEncoder(bCryptPasswordEncoder());
    }
}