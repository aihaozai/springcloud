package com.haozai.cloudoauth.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-08 16:50
 **/
public class SystemJwtTokenEnhancer implements TokenEnhancer {
    /**
     *  自定义一些token属性
     * @param accessToken
     * @param authentication
     * @return OAuth2AccessToken
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication().getName();
        User user = (User) authentication.getUserAuthentication().getPrincipal();
        final Map<String, Object> additionalInformation = new HashMap<>(2);
        additionalInformation.put("userName", userName);
        additionalInformation.put("address", "陕西省西安市");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return accessToken;
    }
}
