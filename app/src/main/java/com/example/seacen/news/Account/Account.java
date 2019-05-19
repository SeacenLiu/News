package com.example.seacen.news.Account;

public class Account {
    interface Handle {
        void callback(UserModel userModel);
    }

    /**
     * 单例
     */
    private  static  class SingletonHolder {
        private static final  Account INSTANCE = new Account();
    }
    private Account () {}
    public  static final Account shared() {
        return SingletonHolder.INSTANCE;
    }

    public UserModel user = null;

    public void login(UserModel model) {
        user = model;
    }

    public void logout() {
        user = null;
    }

    public boolean isLogin() {
        return user != null;
    }

    public boolean isMe(int uid) {
        if (user == null)
            return false;
        return user.id == uid;
    }

}
