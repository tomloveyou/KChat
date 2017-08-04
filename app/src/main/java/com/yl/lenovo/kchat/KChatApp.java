package com.yl.lenovo.kchat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.google.gson.Gson;

import com.yl.lenovo.kchat.bean.MyUser;
import com.yl.lenovo.kchat.utis.SPUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by lenovo on 2017/6/30.
 */

public class KChatApp extends Application {

    public static SharedPreferences sp;// 保存一些需要持久化的小数据
    MyUser bmobUser;
    public static KChatApp application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        // 初始化参数依次为 this, AppId, AppKey
        Bmob.initialize(this, "115e6fffae7337309a4de3668ade696e", "demo");
        sp = getSharedPreferences(SPUtils.SP_NAME, Context.MODE_PRIVATE);
  

    }

    public MyUser getBmobUser() {
        if (bmobUser == null)
            bmobUser = new Gson().fromJson(SPUtils.getString("userinfo"), MyUser.class);
        return bmobUser;
    }

    public void setBmobUser(MyUser bmobUser) {
        this.bmobUser = bmobUser;
    }

    public static KChatApp getInstance() {
        // TODO Auto-generated method stub
        return application;
    }

}