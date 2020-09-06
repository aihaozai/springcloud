package com.haozai.cloudprovider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Program: springCloud
 * @Author: haozai
 * @Create: 2020-04-12 14:11
 **/
@RefreshScope
@RestController
public class TestController {

    @Value("${me}")
    private String me;

    @GetMapping("/me")
    public String me(){
        return this.me;
    }
}
