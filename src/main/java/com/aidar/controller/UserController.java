package com.aidar.controller;

import com.aidar.domain.User;
import com.aidar.repository.UserRepository;
import com.aidar.util.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2017, spring boot 自我学习
 *
 *
 * @version 0.0.1
 * @desc @RestController是@Controller 和 @ResponseBody的结合，@RequestMapping设置路径，头部信息和生成什么样的格式。
 * @date 17-8-2
 */
@RestController
@RequestMapping(value = "/user", headers = "Accept=application/json", produces = "application/json; charset=utf-8")
public class UserController {
    @Autowired UserRepository userRepository;
//    @Autowired private UserDao userDao;

    @ApiOperation(value = "根据用户名查询用户")
    @RequestMapping(value = "findByUname", method = RequestMethod.GET) public String findByUname() {
        User user = this.userRepository.findByUname("e52pmxbuus7jkw88h66og");
        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
        return JSONUtil.toJson(user);
    }

    @ApiOperation(value = "查询所有用户") @RequestMapping(value = "findAll", method = RequestMethod.GET)
    public String findAll() {
        List<User> users = this.userRepository.findByYi();
        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
        return JSONUtil.toJson(users);
    }

//    @ApiOperation(value = "根据邮箱查询用户") @RequestMapping(value = "findByUemail", method = RequestMethod.GET)
//    public String findByUemail() {
//        User user = this.userDao.getById(2);
//        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
//        return JSONUtil.toJson(user);
//    }
}
