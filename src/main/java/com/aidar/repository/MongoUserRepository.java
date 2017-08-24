package com.aidar.repository;


import com.aidar.domain.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoUserRepository extends MongoRepository<MongoUser, Long> {
    MongoUser findByUid(long uid);

}
