package com.aidar.controller;

import com.aidar.domain.Product;
import com.aidar.domain.User;
import com.aidar.repository.ProductRepository;
import com.aidar.repository.UserRepository;
import com.aidar.util.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
@RequestMapping(value = "/product", headers = "Accept=application/json", produces = "application/json; charset=utf-8")
public class ProductController {
    @Autowired ProductRepository productRepository;
//    @Autowired private UserDao userDao;

    @ApiOperation(value = "根据产品ID查询产品")
    @RequestMapping(value = "findByPId", method = RequestMethod.GET) public String findByPId() {
        Product product = this.productRepository.findByPId(109);
        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
        return JSONUtil.toJson(product);
    }

    @ApiOperation(value = "根据产品名查询产品")
    @RequestMapping(value = "findByPNameLike", method = RequestMethod.GET) public String findByPNameLike() {
        List<Product> products = this.productRepository.findByPNameLike("gogogogo");
        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
        return JSONUtil.toJson(products);
    }

    @ApiOperation(value = "存储产品")
    @RequestMapping(value = "save", method = RequestMethod.GET) public String save() {
        Product product = new Product();
        product.setpName("aaa");
        product.setpFile("/ff/ff/ff");
        product.setCreateTime(new Date());
        product.setModTime(new Date());
        this.productRepository.save(product);
        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
        return JSONUtil.toJson(product);
    }


    //    @ApiOperation(value = "根据邮箱查询用户") @RequestMapping(value = "findByUemail", method = RequestMethod.GET)
//    public String findByUemail() {
//        User user = this.userDao.getById(2);
//        //        String result = user.getuId() + " & " + user.getuName() + " & " + user.getuPwd();
//        return JSONUtil.toJson(user);
//    }
}
