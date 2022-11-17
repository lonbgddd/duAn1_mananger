package com.example.duan1_mananger.model;


public class User {
    private String id;
    private String name_user;
    private String avatar;
    private Boolean sex = true;
    private String phone_number;
    private String birthday;
    private String address;
    private String password;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String id, String name_user, String avatar, Boolean sex, String phone_number, String birthday, String address, String password, String email) {
        this.id = id;
        this.name_user = name_user;
        this.avatar = avatar;
        this.sex = sex;
        this.phone_number = phone_number;
        this.birthday = birthday;
        this.address = address;
        this.password = password;
        this.email = email;
    }
}
