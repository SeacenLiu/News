package com.example.seacen.news.Utils.Network;

import com.alibaba.fastjson.JSONObject;

/**
 * 网络请求处理
 */
public  interface SCNetworkHandler {
    void successHandle(JSONObject jsonObject);
    void errorHandle(Exception error);
}

