package com.example.seacen.news.Utils.Network;

/**
 * 网络请求处理
 */
public  interface SCNetworkHandler {
    void successHandle(String bodyStr);
    void errorHandle(Exception error);
}

