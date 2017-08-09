package com.aidar.dto;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2017, spring boot 自我学习

 * @date 17-8-2
 */
public class ListResponse {

    private String resCode;
    private String resMsg;
    private String sign;
    private List data;

    public String getResCode() {
        return resCode;
    }

    public ListResponse setResCode(String resCode) {
        this.resCode = resCode;
        return this;
    }

    public String getResMsg() {
        return resMsg;
    }

    public ListResponse setResMsg(String resMsg) {
        this.resMsg = resMsg;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public ListResponse setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ListResponse setData(List data) {
        this.data = data;
        return this;
    }
}
