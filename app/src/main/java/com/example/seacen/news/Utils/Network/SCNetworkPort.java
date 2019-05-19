package com.example.seacen.news.Utils.Network;

public enum SCNetworkPort {
    // 账户
    Register("/news/register"),
    Login("/news/login"),
    // 新闻
    AllClassify("/news/allclassify"),
    IndexNews("/news/indexnew"),
    Classify("/news/classify"),
    NewsDetail("/news/detail"),
    // 评论
    Comment("/news/comment"),
    AddComment("/news/addcomment"),
    LikesComment("/news/likescomment"),
    Cancellike("/news/cancellike");

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
