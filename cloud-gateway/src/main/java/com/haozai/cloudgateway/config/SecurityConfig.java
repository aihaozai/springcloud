package com.haozai.cloudgateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-22 16:42
 **/
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final String MAX_AGE = "18000L";

    private AccessManager accessManager;



    @Autowired
    public SecurityConfig(AccessManager accessManager){
        this.accessManager = accessManager;
    }

    /**
     * 跨域配置
     */
    private WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                HttpHeaders requestHeaders = request.getHeaders();
                ServerHttpResponse response = ctx.getResponse();
                HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
                HttpHeaders headers = response.getHeaders();
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
                headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
                if (requestMethod != null) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
                }
                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
                headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }


    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) throws Exception{
        //token管理器
        ReactiveAuthenticationManager tokenAuthenticationManager = new ReactiveJdbcAuthenticationManager(systemJwtTokenStore());
        //认证过滤器
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(tokenAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());

        serverHttpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().access(accessManager)
                .and()
                // 跨域过滤器
                .addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS)
                //oauth2认证过滤器
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.FIRST);
        return serverHttpSecurity.build();
    }


//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("haozai-jwt.jks"), "haozai".toCharArray())
//                .getKeyPair("haozai-jwt");
//        http.authorizeExchange()
//               .pathMatchers(HttpMethod.OPTIONS).permitAll()
//                .anyExchange().authenticated();
////                .anyExchange().access(accessManager);
//        ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtSpec = http.oauth2ResourceServer().jwt();
//        jwtSpec.authenticationManager(new ReactiveJdbcAuthenticationManager(systemJwtTokenStore()));
////        http.oauth2ResourceServer().jwt().publicKey((RSAPublicKey)keyPair.getPublic());
//        http.csrf().disable();
//        return http.build();
//    }

    /**
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore systemJwtTokenStore() {
        return new JwtTokenStore(systemJwtAccessTokenConverter());
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
