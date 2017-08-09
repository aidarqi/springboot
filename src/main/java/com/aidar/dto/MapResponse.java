package com.aidar.dto;

import java.util.Map;

/**
 * Copyright (C), 2017, spring boot 自我学习

 * @date 17-8-2
 */
public class MapResponse {

    private String resCode;
    private String resMsg;
    private String sign;
    private Map data;

    public String getResCode() {
        return resCode;
    }

    public MapResponse setResCode(String resCode) {
        this.resCode = resCode;
        return this;
    }

    public String getResMsg() {
        return resMsg;
    }

    public MapResponse setResMsg(String resMsg) {
        this.resMsg = resMsg;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public MapResponse setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public Object getData() {
        return data;
    }

    public MapResponse setData(Map data) {
        this.data = data;
        return this;
    }
}
