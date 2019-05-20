package com.example.seacen.news.Utils.Network;

/**
 * 网络请求方法
 */
public enum SCNetworkMethod {
    GET("GET"), POST("POST"), DELETE("DELETE"), PUT("PUT");
    private String methodName;
    SCNetworkMethod(String name) {
        this.methodName = name;
    }
    public String getName() {
        return methodName;
    }
}