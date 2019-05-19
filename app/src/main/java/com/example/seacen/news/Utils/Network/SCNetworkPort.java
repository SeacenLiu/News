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
    /**
     * 用于拼接URL
     */
    private String suffix;
    SCNetworkPort(String s) {
        this.subpath = s;
    }

    public void setSuffix(Integer integer) {
        this.suffix = String.valueOf(integer);
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String path() {
        if (suffix != null) {
            return root + subpath + "/" + suffix;
        }
        return root + subpath;
    }
}
