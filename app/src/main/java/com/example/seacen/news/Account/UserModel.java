package com.example.seacen.news.Account;

public class UserModel {
    public Integer id;
    public String name;
    public String phone;

    static UserModel testUser() {
        UserModel userModel = new UserModel();
        userModel.id = 1;
        userModel.name = "123";
        return userModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
