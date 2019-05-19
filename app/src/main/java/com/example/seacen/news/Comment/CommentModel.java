package com.example.seacen.news.Comment;

import com.example.seacen.news.Account.UserModel;

public class CommentModel {
    public UserModel user;
    public Detail detail;

    public static class Detail {
        Integer id;
        Integer fromuid;
        Integer likesnum;
        String content;
        Integer newsid;
        String time;
        Integer tocid;

        public Boolean getLiked() {
            return isLiked;
        }

        public void setLiked(Boolean liked) {
            isLiked = liked;
        }

        Boolean isLiked;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getLikesnum() {
            return likesnum;
        }

        public void setLikesnum(Integer likesnum) {
            this.likesnum = likesnum;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Integer getNewsid() {
            return newsid;
        }

        public void setNewsid(Integer newsid) {
            this.newsid = newsid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Integer getFromuid() {
            return fromuid;
        }

        public void setFromuid(Integer fromuid) {
            this.fromuid = fromuid;
        }

        public Integer getTocid() {
            return tocid;
        }

        public void setTocid(Integer tocid) {
            this.tocid = tocid;
        }
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }
}
