package com.eternallove.demo.zuccfairy.modle;

/**
 * Created by Administrator on 2016/12/25. 书p129
 */
public class HomePage {
    private String name;
    private int imageId;
//HomePage类中只有两个字段，name表示条目名称 imageId表示条目对应图片的资源id
    public HomePage(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
