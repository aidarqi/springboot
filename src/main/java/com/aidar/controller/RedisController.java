package com.aidar.controller;

import com.aidar.domain.User;
import com.aidar.service.UserService;
import com.aidar.util.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2017, spring boot 自我学习

 * @date 17-8-8
 */
@RestController
@RequestMapping(value = "/redis", headers = "Accept=application/json", produces = "application/json; charset=utf-8")
public class RedisController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据用户名查询用户")
    @RequestMapping(value = "findByUname", method = RequestMethod.GET) public String findByUname() {
        User user = this.userService.findUserByUname("e52pmxbuus7jkw88h66og");
        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
        return JSONUtil.toJson(user);
    }
}
