package com.eternallove.demo.zuccfairy.modle;

/**
 * @description:
 * @author: eternallove
 * @date: 2016/12/25 15:28
 */

public class ReceivedBean {
    int id;
    private String sender_id;
    private String recipient_id;
    private long timestampe;
    private String message;
    private String picture;

    public ReceivedBean() {
    }

    public ReceivedBean(String sender_id, String recipient_id,long timestampe, String message, String picture) {
        this.sender_id = sender_id;
        this.recipient_id = recipient_id;
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

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
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
