package com.haozai.cloudoauth.config;

import com.haozai.cloudoauth.service.UserServiceDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;


import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-07-18 19:21
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理器authenticationManager 开启密码授权
     */
    private AuthenticationManager authenticationManager;

    /**
     * 验证身份
     */
    private UserServiceDetailImpl userServiceDetail;

    /**
     * 数据源
     */
    private DataSource dataSource;

    /**
     * jwtToken增强，自定义实现token扩展
     */
    @Qualifier("systemJwtTokenEnhancer")
    private TokenEnhancer jwtTokenEnhancer;


    /**
     * token转换器
     */
    @Qualifier("systemJwtAccessTokenConverter")
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * token存储，自定义实现保存到数据库中
     */
    @Qualifier("systemJwtTokenStore")
    private TokenStore tokenStore;

    /**
     * 构造器注入
     * @param authenticationManager
     * @param userServiceDetail
     * @param dataSource
     */
    @Autowired
    public AuthorizationServerConfiguration(AuthenticationManager authenticationManager,UserServiceDetailImpl userServiceDetail,DataSource dataSource) {
        this.authenticationManager = authenticationManager;
        this.userServiceDetail = userServiceDetail;
        this.dataSource = dataSource;
    }

    /**
     * set注入
     * @param jwtTokenEnhancer
     */
    @Autowired
    public void setTokenEnhancer (@Qualifier("systemJwtTokenEnhancer")TokenEnhancer jwtTokenEnhancer) {
        this.jwtTokenEnhancer = jwtTokenEnhancer;
    }

    /**
     * set注入
     * @param jwtAccessTokenConverter
     */
    @Autowired
    public void setJwtAccessTokenConverter (@Qualifier("systemJwtAccessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    /**
     * set注入
     * @param tokenStore
     */
    @Autowired
    public void setTokenStore (@Qualifier("systemJwtTokenStore") TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * 客户端信息，来源与DB
     * @return JdbcClientDetailsService
     */
    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    /**
     * 自定义将自定授权保存数据库
     * @return ApprovalStore
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 将授权码保存数据库
     * @return AuthorizationCodeServices
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    /**
     * 配置客户端详情服务（ClientDetailsService）
     * 客户端详情信息在这里进行初始化
     * 通过数据库来存储调取详情信息
     * 配置客户但详情从数据库读取，默认手动添加到oauth2客户端表中的数据
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService jdbcClientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        super.configure(oauthServer);
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    /**
     * 从数据库获取客户端
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetails());
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints){
        endpoints.tokenStore(tokenStore)
                //配置将授权码存放在oauth_code变中，默认存在内存中
                .authorizationCodeServices(authorizationCodeServices())
                //配置审批存储oauth_approvals，存储用户审批过程，在一个月时间内不用再次审批
                .approvalStore(approvalStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userServiceDetail)
                //支持刷新令牌
                .reuseRefreshTokens(true)
                //支持GET  POST  请求获取token;
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtAccessTokenConverter);
            enhancers.add(jwtTokenEnhancer);
            //将自定义Enhancer加入EnhancerChain的delegates数组中
            enhancerChain.setTokenEnhancers(enhancers);
            //为什么不直接把jwtTokenEnhancer加在这个位置呢？
            endpoints.tokenEnhancer(enhancerChain)
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }
}
