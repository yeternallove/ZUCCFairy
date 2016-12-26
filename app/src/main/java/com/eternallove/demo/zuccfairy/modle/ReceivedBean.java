package com.eternallove.demo.zuccfairy.modle;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/25 15:28
 */

public class ReceivedBean {
    int id;
    private String user_id;
    private long timestampe;
    private String message;
    private String picture;
    public ReceivedBean(){}
    public ReceivedBean(String user_id,long timestampe,String message,String picture){
       this.user_id = user_id;
       this.timestampe = timestampe;
       this.message = message;
       this.picture = picture;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getTimestampe() {
        return timestampe;
    }

    public void setTimestampe(long timestampe) {
        this.timestampe = timestampe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
