package com.haozai.cloudoauth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-08 20:24
 **/

@Data
@TableName("role")
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
