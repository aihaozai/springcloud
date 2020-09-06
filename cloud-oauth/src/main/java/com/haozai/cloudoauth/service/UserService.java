package com.haozai.cloudoauth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haozai.cloudoauth.entity.User;
import com.haozai.cloudoauth.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-16 15:45
 **/

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public IPage<User> selectUserPage(Page<User> page) {
        return this.baseMapper.selectUserPage(page);
    }
}
