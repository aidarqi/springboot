package com.aidar.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Copyright (C), 2017, spring boot 自我学习
 * @version 0.0.1
 * @desc JSON utiljackson-core-asl
 * @date 2017-04-19 09:14:23
 */
public class JSONUtil {
    private static final Logger logger = LoggerFactory.getLogger(JSONUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转为JSON字符串
     */
    public static String toJson(Object object) {
        String str = null;
        try {
            str = objectMapper.writer().writeValueAsString(object);
        } catch (IOException e) {
            logger.info("对象转为JSON字符串失败", e);
        }
        return str;
    }

    /**
     * JSON字符串转为对象
     *
     * @param mapper
     * @param valueType 目标对象的类型
     * @param json JSON字符串
     * @return object 目标对象(转换失败返回 null)
     */
    public static <T> T toObject(ObjectMapper mapper, String json, Class<T> valueType) {
        T object = null;
        try {
            object = mapper.reader(valueType).readValue(json);
        } catch (Exception e) {
            logger.info("JSON字符串转为对象", e);
        }
        return object;
    }

    /**
     * JSON字符串转为对象
     *
     * @param valueType 目标对象的类型
     * @param json JSON字符串
     * @return object 目标对象(转换失败返回 null)
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        T object = null;
        try {
            object = objectMapper.reader(valueType).readValue(json);
        } catch (Exception e) {
            logger.info("JSON字符串转为对象", e);
        }
        return object;
    }

    /**
     * JSON字符串转为泛型
     *
     * @param json JSON字符串
     * @param valueTypeRef 目标对象的类型
     * @return T
     */
    public static <T> T toObject(String json, TypeReference<?> valueTypeRef) {
        T object = null;
        try {
            object = objectMapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
            logger.info("JSON字符串转为对象", e);
        }
        return object;
    }

    private static List<Map<String, Object>> json2List(Object json) {
        JSONArray jsonArr = (JSONArray) json;
        List<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < jsonArr.size(); ++i) {
            arrList.add(strJson2Map(jsonArr.getString(i)));
        }
        return arrList;
    }

//    private static List<Map<String, Object>> json2List(String json) {
//        JSONObject jsonObject = JSONObject.parseObject(json);
//        List<Map<String, Object>> arrList = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < jsonObject.size(); ++i) {
//            arrList.add(strJson2Map(jsonObject.getValue));
//        }
//        return arrList;
//    }

    public static Map<String, Object> strJson2Map(String json) {
        JSONObject jsonObject = JSONObject.parseObject(json);
        Map<String, Object> resMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> param = (Map.Entry<String, Object>) it.next();
            if (param.getValue() instanceof JSONObject) {
                System.out.println("JSONObject: " + param);
                resMap.put(param.getKey(), strJson2Map(param.getValue().toString()));
            } else if (param.getValue() instanceof JSONArray) {
                System.out.println("JSONOArray: " + param);
                resMap.put(param.getKey(), json2List(param.getValue()));
            } else {
                System.out.println("else: " + param);
                resMap.put(param.getKey(), JSONObject.toJSONString(param.getValue(), SerializerFeature.WriteClassName));
            }
        }
        return resMap;
    }
}
