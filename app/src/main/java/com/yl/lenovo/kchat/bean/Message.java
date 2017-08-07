package com.yl.lenovo.kchat.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2017/8/7.
 */

public class Message extends BmobObject {
    private  String alert;
    private String content;

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
