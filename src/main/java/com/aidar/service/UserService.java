package com.aidar.service;

import com.aidar.domain.User;
import com.aidar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Copyright (C), 2017, spring boot 自我学习

 * @date 17-8-7
 */
@Service
public interface UserService {
    User findUserByUname(String uname);
    void saveUser(User user);
    void updateUser(int uid);
    void deleteUser(int uid);
}
