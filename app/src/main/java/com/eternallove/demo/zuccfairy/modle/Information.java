package com.eternallove.demo.zuccfairy.modle;

/**
 * Created by Administrator on 2016/12/26.
 */
public class Information {
    private String name;
    private int imageId;
    private String message;
    //Information 类中有三个字段，name表示条目名称 imageId表示条目对应图片的资源id,message是具体内容
    public Information(String name, int imageId, String message) {
        this.name = name;
        this.imageId = imageId;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getMessage(){
        return message;
    }
}
