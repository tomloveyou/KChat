package com.yl.lenovo.kchat.bean;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2017/8/7.
 */

public class Message extends BmobObject {
    private  String alert;
    private String content;

    public String getAlert() {
        String result="";
        try {
            JSONObject object=new JSONObject(alert);
            result= object.optString("alert");
        } catch (JSONException e) {
            e.printStackTrace();
            result= alert;
        }
        return result;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getContent() {
        String result="";
        try {
            JSONObject object=new JSONObject(alert);
            result= object.optString("content");
        } catch (JSONException e) {
            e.printStackTrace();
            result= content;
        }
        return result;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
