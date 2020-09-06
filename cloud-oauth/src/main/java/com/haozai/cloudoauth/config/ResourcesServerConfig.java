package com.haozai.cloudoauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-08 16:48
 **/
@Configuration
@EnableResourceServer
public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {

    @Qualifier("tokenServices")
    private DefaultTokenServices tokenServices;

    @Qualifier("systemJwtTokenStore")
    private TokenStore tokenStore;

    /**
     * set注入
     * @param tokenStore
     */
    @Autowired
    public void setTokenStore (@Qualifier("systemJwtTokenStore") TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * set注入
     * @param tokenServices
     */
    @Autowired
    public void setDefaultTokenServices (@Qualifier("tokenServices") DefaultTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    /**
     * @description 资源id可再次手动设置任意字符串，如果不设置，则需要在数据oauth_client_details中的resource_ids填写固定值"oauth2-resource"
     * @param resources
     * @throws Exception
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices);
        resources.tokenStore(tokenStore);
        super.configure(resources);
    }

    /**
     * @description 配置需要拦截的资源，这里可扩展的比较多，自由发挥
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/**","/oauth/**","/login","/health", "/css/**").permitAll()
                .anyRequest().authenticated();
    }
}
