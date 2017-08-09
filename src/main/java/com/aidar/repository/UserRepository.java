package com.aidar.repository;

import com.aidar.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

/**
 * Copyright (C), 2017, spring boot 自我学习
 *
 * @author bubingy
 * @version 0.0.1
 * @desc 普通用户登录/注册
 * @date 17-6-5
 */
@Repository
//@Table(name="User")
//@Qualifier("userRepository")
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    @Query("select t from User t where t.uname=?1")
    User findByUname(String uname);

    User findByUid(int uid);

    @Query(value = "select * from User limit 10",nativeQuery = true)
    List<User> findByYi();
}
