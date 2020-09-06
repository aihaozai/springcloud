package com.haozai.cloudoauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @program: springCloud
 * @author: haozai
 * @description:
 * @date: 2020-07-18 19:21
 **/
@RefreshScope
@EnableDiscoveryClient
@MapperScan("com.haozai.cloudoauth.mapper")
@SpringBootApplication(scanBasePackages = {"com.haozai.cloudoauth","com.haozai.cloudbase"})
public class CloudOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudOauthApplication.class, args);
    }

}
