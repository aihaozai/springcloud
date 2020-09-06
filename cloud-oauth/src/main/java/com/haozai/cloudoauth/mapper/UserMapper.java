package com.haozai.cloudoauth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.haozai.cloudoauth.entity.User;
import java.util.List;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-16 15:45
 **/
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectUserPage(Page<User> page);
}
