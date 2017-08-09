package com.aidar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Copyright (C), 2017, spring boot 自我学习
 * @desc @SpringBootApplication是@Configuration, @EnableAutoConfiguration 和 @ComponentScan三个联合起来的一个注解。
 *       @Configuration说明这是一个配置类，@EnableAutoConfiguration允许自动配置，@ComponentScan扫描。
 * @date 17-8-2
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan({"com.aidar"})
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
