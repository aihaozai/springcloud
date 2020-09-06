package com.haozai.cloudoauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import java.security.KeyPair;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-08 16:39
 **/
@Configuration
public class JwtTokenConfig {

    /**
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer systemJwtTokenEnhancer() {
        return new SystemJwtTokenEnhancer();
    }

    /**
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore systemJwtTokenStore() {
        return new JwtTokenStore(systemJwtAccessTokenConverter());
    }

    /**
     * @return DefaultTokenServices
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(systemJwtTokenStore());
        return defaultTokenServices;
    }
    /**
     * token生成处理：指定签名
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter systemJwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("haozai-jwt.jks"), "haozai".toCharArray())
                .getKeyPair("haozai-jwt");
        //非对称加密
        accessTokenConverter.setKeyPair(keyPair);
        return accessTokenConverter;
    }
}
