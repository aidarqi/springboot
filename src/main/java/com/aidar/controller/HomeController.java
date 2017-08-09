package com.aidar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Copyright (C), 2017, spring boot 自我学习

 * @date 17-8-2
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "redirect:swagger-ui.html";
    }
}
