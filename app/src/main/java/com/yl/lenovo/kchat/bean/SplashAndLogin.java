package com.yl.lenovo.kchat.bean;



import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2017/7/4.
 */

public class SplashAndLogin extends BmobObject {

    private BmobFile splash_url;
    private BmobFile login_url;

    public BmobFile getSplash_url() {
        return splash_url;
    }

    public void setSplash_url(BmobFile splash_url) {
        this.splash_url = splash_url;
    }

    public BmobFile getLogin_url() {
        return login_url;
    }

    public void setLogin_url(BmobFile login_url) {
        this.login_url = login_url;
    }
}
