package com.eternallove.demo.zuccfairy.modle;

/**
 * @description:
 * @author: eternallove
 * @date: 2017/1/1 21:15
 */

public class CardBean {
    private int id;
    private String user_id;
    private int chat_id;
    private long timestampe;
    private int num;//人数
    private int days;
    private int percentage;

    public CardBean(){}

    public CardBean(String user_id,int chat_id,long timestampe,int num,int days,int percentage){
       this.user_id = user_id;
       this.chat_id = chat_id;
       this.timestampe = timestampe;
       this.num = num;
        this.days = days;
       this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
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

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public long getTimestampe() {
        return timestampe;
    }

    public void setTimestampe(long timestampe) {
        this.timestampe = timestampe;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
