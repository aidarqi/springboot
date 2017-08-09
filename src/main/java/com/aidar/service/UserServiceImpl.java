package com.aidar.service;

import com.aidar.domain.User;
import com.aidar.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * Copyright (C), 2017, spring boot 自我学习

 * @date 17-8-8
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override public User findUserByUname(String uname) {
        String key = "user_" + uname;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        System.out.println("hasKey: " + hasKey);
        if(hasKey) {
            User user = operations.get(key);
            logger.info("从缓存中获取用户: " + key +"---" + user.toString());

            return user;
        } else {
            User user = userRepository.findByUname(uname);
            operations.set(key, user,30, TimeUnit.MINUTES);
            logger.info("将用户插入缓存: " + key +"---" + user.toString());
            return user;
        }
    }

    @Override public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override public void updateUser(int uid) {
        User user = userRepository.findByUid(uid);
        String key = "user_" + user.getUname();
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey) {
            redisTemplate.delete(key);
            logger.info("从缓存中删除城市: " + user.toString());
        }
    }

    @Override public void deleteUser(int uid) {
        User user = userRepository.findByUid(uid);
        String key = "user_" + user.getUname();
        userRepository.delete(user);
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey) {
            redisTemplate.delete(key);
            logger.info("从缓存中删除城市: " + user.toString());
        }
    }
}
