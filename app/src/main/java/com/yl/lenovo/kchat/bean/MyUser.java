package com.yl.lenovo.kchat.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by lenovo on 2017/7/4.
 */

public class MyUser extends BmobUser {
    private int sex=0;//0是男1是女
    private String user_avator;
    private String nickname;
    private BmobRelation favorite;

    public BmobRelation getFavorite() {
        return favorite;
    }

    public void setFavorite(BmobRelation favorite) {
        this.favorite = favorite;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUser_avator() {
        return user_avator;
    }

    public void setUser_avator(String user_avator) {
        this.user_avator = user_avator;
    }
}
