package com.haozai.cloudoauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.util.List;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-08 20:20
 **/

@Data
@TableName("user")
public class User implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String username;

    private String password;

    private List<Role> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
