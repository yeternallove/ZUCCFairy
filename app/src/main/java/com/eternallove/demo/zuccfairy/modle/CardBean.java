package com.eternallove.demo.zuccfairy.modle;

/**
 * @description:
 * @author: eternallove
 * @date: 2017/1/1 21:15
 */

public class CardBean {
    private int id;
    private long timestampe;
    private int num;//人数
    private int percentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
