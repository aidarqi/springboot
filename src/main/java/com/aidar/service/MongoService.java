package com.aidar.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

import com.aidar.domain.MongoUser;
import com.aidar.repository.MongoUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MongoService {

    private static final Logger logger = LoggerFactory.getLogger(MongoService.class);

    @Autowired
	private MongoUserRepository mongoEntityRepository;

	/**
	 * Create "count" Mongo entities starting with id "startId."
	 * Do this first for creating, say, 110,000 entities.
	 *
	 * @param count
	 * @param startId
	 */
    public void createEntities(long count, long startId) {

		for (long i = 0; i < count; i++) {

			long id = startId + i;
			
			MongoUser mongoUser = new MongoUser();
			mongoUser.setUname("qdm " + id);
			mongoUser.setUid(id);
			mongoUser.setCreatetime(new Date());
			mongoEntityRepository.save(mongoUser);
		}
	}

	/**
	 * Retrieve "count" entities starting from 1 in step "step."
	 * Do this to retrieve, say 200 records in step, say 500
	 *
	 * @param count
	 * @param step
	 */
    public List<MongoUser> retrieveEntities(long count, long step) {
    	List<MongoUser> list = new ArrayList<>();

    	long nextId = 20;
    	MongoUser user1 = mongoEntityRepository.findOne(20L);
		System.out.println(user1);

    	for (long i = 0; i < count; i++) {
			MongoUser user = mongoEntityRepository.findByUid(nextId);
			logger.info("user{} " , user);
			list.add(user);
			nextId += step;
		}
		return list;
	}

}
