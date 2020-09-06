package com.haozai.cloudbase.config.mybatis;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-08-16 14:23
 **/
@Slf4j
@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.info("MybatisPlus PaginationInterceptor Loading Succeeded");
        return new PaginationInterceptor();
    }

    /**
     * 乐观锁插件
     * @return OptimisticLockerInterceptor
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }
}
