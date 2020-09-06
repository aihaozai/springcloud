package com.haozai.cloudoauth.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

/**
 * @program: springCloud
 * @author: haozai
 * @description: OAUTH2身份验证服务
 * @date: 2020-08-08 20:06
 **/
@Service
public class UserServiceDetailImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("haozai".equalsIgnoreCase(username)) {
            User user = mockUser();
            return user;
        }
        return null;
    }

    private User mockUser() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("admin"));
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String pwd = bcryptPasswordEncoder.encode("123456");
        return new User("haozai",pwd,authorities);
    }

}