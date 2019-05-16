package com.example.seacen.news.Account;

public class Account {
    /**
     * 单例
     */
    private  static  class SingletonHolder {
        private static final  Account INSTANCE = new Account();
    }
    private Account () {

    }
    public  static final Account shared() {
        return SingletonHolder.INSTANCE;
    }

    public UserModel user = null;
}
