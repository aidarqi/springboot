package com.aidar.controller;

import com.aidar.domain.MongoUser;
import com.aidar.repository.MongoUserRepository;
import com.aidar.service.MongoService;
import com.aidar.util.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MongoController {
	
    private static final Logger logger = LoggerFactory.getLogger(MongoController.class);

	@Autowired
	private MongoUserRepository mongoEntityRepository;
	
	@Autowired
	private MongoService mongoService;
	
	/**
	 * Creates a new Mongo entity. Used for testing whether the environment is working ok
	 *
	 * @param descr
	 * @return
	 */
	@ApiOperation(value = "插入一个mongoUser实体")
	@RequestMapping(value = "/insert", method = RequestMethod.GET)
	public String insert(@RequestParam("uname") String uname, @RequestParam("uid") long uid, @RequestParam("uemail") String uemail) {

		MongoUser mongoUser = new MongoUser();

		mongoUser.setUid(uid);
		mongoUser.setUname(uname);
		mongoUser.setUemail(uemail);

		mongoEntityRepository.save(mongoUser);

		return Long.toString(mongoUser.getUid());

	}

	/**
	 * See {@link MongoService#createEntities(long, long)} for details.
	 *
	 * @param descr
	 * @return
	 */

	@ApiOperation(value = "插入多个mongoUser实体")
	@RequestMapping(value = "/inserts", method = RequestMethod.GET)
	public String inserts(@RequestParam("count") long count, @RequestParam("startId") long startId)
	{
		logger.info("Creating " + count + " Mongo entities starting from " + startId);

		mongoService.createEntities(count, startId);

		logger.info("Creating Mongo entities complete");

		return "Success";
	}


	/**
	 * See {@link MongoService#retrieveEntities(long, long)} for details.
	 *
	 * @param descr
	 * @return
	 */
	@ApiOperation(value = "取回实体")
	@RequestMapping(value = "retrieve", method = RequestMethod.GET)
	public String retrieveMongoEntities(@RequestParam("count") long count, @RequestParam("step") long step)
	{
		logger.info("Retrieving " + count + " Mongo entities in step " + step);
		List<MongoUser> list = mongoService.retrieveEntities(count, step);
		logger.info("Retrieving Mongo entities complete");

		return JSONUtil.toJson(list);
	}

}
