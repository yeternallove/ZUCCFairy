package com.eternallove.demo.zuccfairy.modle;

import com.eternallove.demo.zuccfairy.R;

/**
 * @description:
 * @author: eternallove
 * @date: 2017/1/2 15:34
 */

public class ReplyBean {
    String key;
    String content;

    public ReplyBean(){}
    public ReplyBean(String key,String content){
        this.key = key;
        this.content = content;

    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
