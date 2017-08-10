package com.yl.lenovo.kchat.stick.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class ChannelEntity extends BmobObject implements Serializable {

    private String title;
    private String tips;
    private String image_url;

    public ChannelEntity() {
    }

    public ChannelEntity(String title, String tips, String image_url) {
        this.title = title;
        this.tips = tips;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
