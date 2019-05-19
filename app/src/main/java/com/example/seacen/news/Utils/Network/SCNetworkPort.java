package com.example.seacen.news.Utils.Network;

public enum SCNetworkPort {
    Register("/news/register"),
    Login("/news/login"),
    IndexNews("/news/indexnew"),
    NewsDetail("/news/detail"),
    AllClassify("/news/allclassify"),
    Classify("/news/classify"),

    Comment("/news/comment"),
    AddComment("/news/addcomment");

    static String root = "http://47.106.248.255:8080";

    private String subpath;
    SCNetworkPort(String s) {
        this.subpath = s;
    }

    public void addSuffix(Integer integer) {
        addSuffix(String.valueOf(integer));
    }

    public void addSuffix(String suffix) {
        this.subpath += "/";
        this.subpath += suffix;
    }

    public String path() {
        return root + subpath;
    }
}
