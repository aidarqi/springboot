package com.aidar;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Copyright (C), 2017, spring boot 自我学习
 * @desc @SpringBootApplication是@Configuration, @EnableAutoConfiguration 和 @ComponentScan三个联合起来的一个注解。
 *       @Configuration说明这是一个配置类，@EnableAutoConfiguration允许自动配置，@ComponentScan扫描。
 *       @EnableTransactionManagement关于事务管理器，不管是JPA还是JDBC等都实现自接口 PlatformTransactionManager
 *         如果你添加的是 spring-boot-starter-jdbc 依赖，框架会默认注入 DataSourceTransactionManager 实例。
 *         如果你添加的是 spring-boot-starter-data-jpa 依赖，框架会默认注入 JpaTransactionManager 实例。
 * @date 17-8-2
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan({"com.aidar"})
@EnableCaching
@EnableTransactionManagement
public class Application {

    //人为指定使用哪个事务管理器, 其中 dataSource 框架会自动为我们注入
//    @Bean
//    public PlatformTransactionManager txManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//    @Bean
//    public Object testBean(PlatformTransactionManager platformTransactionManager){
//        System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
//        return new Object();
//    }
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
