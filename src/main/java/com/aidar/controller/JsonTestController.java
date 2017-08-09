package com.aidar.controller;
import com.aidar.dto.ListResponse;
import com.aidar.dto.MapResponse;
import com.aidar.util.JSONUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2017, spring boot 自我学习
 *
 *
 * @version 0.0.1
 * @desc @RestController是@Controller 和 @ResponseBody的结合，@RequestMapping设置路径，头部信息和生成什么样的格式。
 * @date 17-8-2
 */
@RestController
@RequestMapping(value = "/test", headers = "Accept=application/json", produces = "application/json; charset=utf-8")
public class JsonTestController {
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {
        String result = JSONUtil.toJson("success");
        System.out.println(result);
        return result;
    }

    @ApiOperation(value = "json字符串转map输出")
    @RequestMapping(value = "test1", method = RequestMethod.GET)
    public String test1() {
//        List list = new ArrayList();
        String json2 =  "{\"entityName\":\"李四\",\"entityId\":\"" + 1
            + "\",\"address\":\"重庆市\",\"gender\":\"男\",\"age\":\"186602***13\",\"orgName\":\"重庆**大学\",\"orgAddress\":\"重庆市\",\"phone\":\"188********\",\"email\":\"188********@qq.com\",\"loanAmount\":\"10000\",\"loanDate\":\"\",\"loanTerm\":\"\",\"overAmount\":\"6011.34\",\"state\":\"逾期\",\"overDate\":\"\",\"publishDate\":\"2014-11-03\",\"publishSource\":\"贷贷红\"}";
//        String result = JSONUtil.toJson(JSONUtil.toObject(json2, Map.class));
//        System.out.println(result);
//        list.add(JSONUtil.toObject(json2, Map.class));
        MapResponse response = new MapResponse();
        response.setResCode("0000")
            .setResMsg("success")
            .setData(JSONUtil.toObject(json2, Map.class));
        return JSONUtil.toJson(response);
    }

    @ApiOperation(value = "json字符串转list输出")
    @RequestMapping(value = "test5", method = RequestMethod.GET)
    public String test5() {
        //        List list = new ArrayList();
        String json2 =  "{\"entityName\":\"李四\",\"entityId\":\"" + 1
            + "\",\"address\":\"重庆市\",\"gender\":\"男\",\"age\":\"186602***13\",\"orgName\":\"重庆**大学\",\"orgAddress\":\"重庆市\",\"phone\":\"188********\",\"email\":\"188********@qq.com\",\"loanAmount\":\"10000\",\"loanDate\":\"\",\"loanTerm\":\"\",\"overAmount\":\"6011.34\",\"state\":\"逾期\",\"overDate\":\"\",\"publishDate\":\"2014-11-03\",\"publishSource\":\"贷贷红\"}";
        //        String result = JSONUtil.toJson(JSONUtil.toObject(json2, Map.class));
        //        System.out.println(result);
        //        list.add(JSONUtil.toObject(json2, Map.class));
        ListResponse response = new ListResponse();
        response.setResCode("0000")
            .setResMsg("success")
            .setData(JSONUtil.toObject(json2, List.class));
        return JSONUtil.toJson(response);
    }

    @ApiOperation(value = "jm json字符串转map输出")
    @RequestMapping(value = "test2", method = RequestMethod.GET)
    public String test2() {
        String json2 =  "{\"entityName\":\"李四\",\"entityId\":\"" + 1
            + "\",\"address\":\"重庆市\",\"gender\":\"男\",\"age\":\"186602***13\",\"orgName\":\"重庆**大学\",\"orgAddress\":\"重庆市\",\"phone\":\"188********\",\"email\":\"188********@qq.com\",\"loanAmount\":\"10000\",\"loanDate\":\"\",\"loanTerm\":\"\",\"overAmount\":\"6011.34\",\"state\":\"逾期\",\"overDate\":\"\",\"publishDate\":\"2014-11-03\",\"publishSource\":\"贷贷红\"}";
//        String result = JSONUtil.toJson(JSONUtil.strJson2Map(json2));
//        System.out.println(result);

        MapResponse response = new MapResponse();
        response.setResCode("0000")
            .setResMsg("success")
            .setData(JSONUtil.strJson2Map(json2));
        return JSONUtil.toJson(response).replaceAll("\\\\\"", "");
    }

//    @ApiOperation(value = "jm json字符串转list输出")
//    @RequestMapping(value = "test6", method = RequestMethod.GET)
//    public String test6() {
//        //        List list = new ArrayList();
//        String json2 =  "{\"entityName\":\"李四\",\"entityId\":\"" + 1
//            + "\",\"address\":\"重庆市\",\"gender\":\"男\",\"age\":\"186602***13\",\"orgName\":\"重庆**大学\",\"orgAddress\":\"重庆市\",\"phone\":\"188********\",\"email\":\"188********@qq.com\",\"loanAmount\":\"10000\",\"loanDate\":\"\",\"loanTerm\":\"\",\"overAmount\":\"6011.34\",\"state\":\"逾期\",\"overDate\":\"\",\"publishDate\":\"2014-11-03\",\"publishSource\":\"贷贷红\"}";
//        //        String result = JSONUtil.toJson(JSONUtil.toObject(json2, Map.class));
//        //        System.out.println(result);
//        //        list.add(JSONUtil.toObject(json2, Map.class));
//        ListResponse response = new ListResponse();
//        response.setResCode("0000")
//            .setResMsg("success")
//            .setData(JSONUtil.json2List(json2));
//        return JSONUtil.toJson(response);
    //    }

    @ApiOperation(value = "xml字符串转map输出")
    @RequestMapping(value = "test3", method = RequestMethod.GET)
    public String test3() throws Exception {
        String json2 =  "<note>\n" + "<to>George</to>\n" + "<from>John</from>\n"
            + "<heading>Reminder</heading>\n" + "<body>Don't forget the meeting!</body>\n"
            + "</note>";
        XmlMapper xmlMapper = new XmlMapper();
        Map entries = xmlMapper.readValue(json2, Map.class);
        MapResponse response = new MapResponse();
        response.setResCode("0000")
            .setResMsg("success")
            .setData(entries);
        return JSONUtil.toJson(response);
    }

    @ApiOperation(value = "xml字符串转list输出")
    @RequestMapping(value = "test4", method = RequestMethod.GET)
    public String test4() throws Exception {
        String json2 =  "<note>\n" + "<to>George</to>\n" + "<from>John</from>\n"
            + "<heading>Reminder</heading>\n" + "<body>Don't forget the meeting!</body>\n"
            + "</note>";
        XmlMapper xmlMapper = new XmlMapper();
        List entries = xmlMapper.readValue(json2, List.class);
        ListResponse response = new ListResponse();
        response.setResCode("0000")
            .setResMsg("success")
            .setData(entries);
        return JSONUtil.toJson(response);
    }
}
