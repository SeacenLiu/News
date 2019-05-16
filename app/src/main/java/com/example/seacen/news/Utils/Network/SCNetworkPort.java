package com.example.seacen.news.Utils.Network;

public enum SCNetworkPort {
    Register("/news/register"),
    Login("news/login");

    private String root = "http://47.106.248.255:8080/";

    private String subpath;
    SCNetworkPort(String s) {
        this.subpath = s;
    }

    public String path() {
        return root + subpath;
    }
}