package com.example.seacen.news.Utils.Network;

import com.alibaba.fastjson.JSON;

public class SCJsonResponse<T> {
    int status;
    T data;
    String msg;

    public static final SCJsonResponse parseObject(String json) {
        SCJsonResponse obj = JSON.parseObject(json, SCJsonResponse.class);
        return obj;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
