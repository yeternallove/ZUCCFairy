package com.eternallove.demo.zuccfairy.modle;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/12
 */
public class UserBean {
    private String user_id;
    private String account;
    private String pwd;
    private String name;
    private String avatar;
    private String data;
    public UserBean(){}
    public UserBean(String user_id,String account,String pwd,String name,String avatar,String data){
        this.user_id = user_id;
        this.account = account;
        this.pwd = pwd;
        this.name = name;
        this.avatar = avatar;
        this.data = data;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
